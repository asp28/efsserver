package uk.co.ankeetpatel.encryptedfilesystem.efsserver.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.ACL.LocalPermissionService;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.Exception.NoAccessToFileException;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.algorithms.CipherUtility;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.AWSObject;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests.DownloadRequest;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests.FileUploadRequest;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests.PermissionRequest;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests.UploadRequest;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses.DownloadResponse;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses.FileUploadResponse;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses.MessageResponse;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.FileRepository;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.UserRepository;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.security.services.S3Services;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CipherUtility cipherUtility;

    @Autowired
    S3Services s3Services;

    @Autowired
    LocalPermissionService permissionService;


    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR') or hasRole('SUPERADMIN')")
    @PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'WRITE')")
    public List<File> allAccess() {
        return fileRepository.findAll();
    }

    //Request key works - need to make the key dynamic...generate key and send
    @GetMapping("/upload/request")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR') or hasRole('SUPERADMIN')")
    public ResponseEntity<?> uploadRequestFile(@Valid @RequestBody FileUploadRequest fileUploadRequest, Authentication authentication) {
        KeyPair keyPair = cipherUtility.getKeyPair();
        PublicKey publicKey;
        PrivateKey privateKey;
        try {
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            File file = new File(fileUploadRequest.getFileName());
            file.setFileName(fileUploadRequest.getFileName());
            file.setDateUploaded(new Date());
            file.setPublicKey(cipherUtility.encodeKey(publicKey));
            file.setPrivateKey(cipherUtility.encodeKey(privateKey));
            fileRepository.save(file);

            System.out.println(authentication.getName());

            permissionService.addPermissionForUser(file, BasePermission.READ, authentication.getName());
            permissionService.addPermissionForUser(file, BasePermission.WRITE, authentication.getName());

            return ResponseEntity.ok(new FileUploadResponse(file.getId(), file.getPublicKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(new MessageResponse("Error: Could not generate new set of keys."));
    }


    @PreAuthorize("hasPermission(returnObject, 'write')")
    private File getFileIfTheyCanWrite(Long id) throws NoAccessToFileException {
        Optional<File> file = fileRepository.findById(id);
        if (file.isPresent()) {
            return file.get();
        }
        throw new NoAccessToFileException();
    }

    @PreAuthorize("hasPermission(returnObject, 'read')")
    private File getFileIfTheyCanRead(Long id) throws NoAccessToFileException {
        Optional<File> file = fileRepository.findById(id);
        if (file.isPresent()) {
            return file.get();
        }
        throw new NoAccessToFileException();
    }

    @PreAuthorize("hasPermission(returnObject, 'administration')")
    private File getFileID(Long id) throws NoAccessToFileException {
        Optional<File> file = fileRepository.findById(id);
        if (file.isPresent()) {
            return file.get();
        }
        throw new NoAccessToFileException();
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('[USER, MODERATOR, ADMINISTRATOR, SUPERADMIN]')")
    public ResponseEntity<?> uploadFile(@Valid @RequestBody UploadRequest UploadRequest) {
        File chosenFile = null;
        try {
            chosenFile = getFileIfTheyCanWrite(UploadRequest.getId());
        } catch (NoAccessToFileException e) {
            return ResponseEntity.ok(new MessageResponse(e.getMessage()));
        }

        if (chosenFile != null) {

            AWSObject newfile = new AWSObject(UploadRequest.getBytes(), UploadRequest.getFileName());
            try {
                java.io.File file = new java.io.File("src/main/java/files/" + UploadRequest.getId() + ".txt");
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(newfile);
                s3Services.uploadFile(UploadRequest.getId() + ".txt", "src/main/java/files/" + UploadRequest.getId() + ".txt");
                chosenFile.setDateUploaded(new Date());
                oos.close();
                fos.close();
                file.delete();
                chosenFile.setFileUploaded(true);
                fileRepository.save(chosenFile);
                return ResponseEntity.ok(new MessageResponse("File upload successful."));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return ResponseEntity.ok(new MessageResponse("Error: Invalid file id"));
        }

        return ResponseEntity.ok(new MessageResponse("Error uploading file. Try again."));
    }

    @GetMapping("/download")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR') or hasRole('SUPERADMIN')")
    public ResponseEntity<?> downloadFile(@Valid @RequestBody DownloadRequest downloadRequest) {
        File chosenFile = null;
        try {
             chosenFile = getFileIfTheyCanRead(downloadRequest.getId());
        } catch (NoAccessToFileException ex) {
            return ResponseEntity.ok(new MessageResponse(ex.getMessage()));
        }

        if (chosenFile != null) {
            s3Services.downloadFile(downloadRequest.getId() + ".txt");
            try {
                java.io.File file = new java.io.File("src/main/java/files/" + downloadRequest.getId() + ".txt");
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                AWSObject obj = (AWSObject) ois.readObject();

                byte[] decoded = cipherUtility.decryption(obj.getFile(), cipherUtility.decodePrivateKey(chosenFile.getPrivateKey()));
                ArrayList<byte[]> encoded = cipherUtility.encryption(decoded, cipherUtility.decodePublicKey(downloadRequest.getPublicKey()));

                ois.close();
                fis.close();
                file.delete();

                return ResponseEntity.ok(new DownloadResponse(downloadRequest.getId(), encoded, obj.getOriginalName()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok(new MessageResponse("File downloaded."));
        } else {
            return ResponseEntity.ok(new MessageResponse("File ID not found."));
        }
        //return ResponseEntity.ok(new MessageResponse("Error: Download request corrupted."));
    }

    @GetMapping("/addpermission")
    @PreAuthorize("hasAnyRole('[USER, MODERATOR, ADMINISTRATOR, SUPERADMIN]')")
    public ResponseEntity<?> addPermission(@Valid @RequestBody PermissionRequest permissionRequest, Authentication authentication) {

        File file = null;
        try {
            file = getFileID(permissionRequest.getFileID());
        } catch (NoAccessToFileException e) {
            return ResponseEntity.ok(new MessageResponse(e.getMessage()));
        }

        String username = userRepository.findById(permissionRequest.getUserID()).get().getUsername();

        if (file != null) {
            for (String s : permissionRequest.getPermissiontypes()) {
                switch(s) {
                    case "read":
                        permissionService.addPermissionForUser(file, BasePermission.READ, username );
                        break;
                    case "write":
                        permissionService.addPermissionForUser(file, BasePermission.WRITE, username);
                        break;
                    case "admin":
                        permissionService.addPermissionForUser(file, BasePermission.ADMINISTRATION, username);
                        break;
                    case "delete":
                        permissionService.addPermissionForUser(file, BasePermission.DELETE, username);
                        break;
                }
            }
            return ResponseEntity.ok(new MessageResponse("Permissions successfully updated."));
        } else {
            return ResponseEntity.ok(new MessageResponse("File not found or you may not have permission to add users to this file. Please contact a moderator for more support."));
        }
    }

    @GetMapping("/deletepermission")
    @PreAuthorize("hasAnyRole('[USER, MODERATOR, ADMINISTRATOR, SUPERADMIN]')")
    public ResponseEntity<?> deletePermission(@Valid @RequestBody PermissionRequest permissionRequest, Authentication authentication) {

        File file = null;
        try {
            file = getFileID(permissionRequest.getFileID());
        } catch (NoAccessToFileException e) {
            return ResponseEntity.ok(new MessageResponse(e.getMessage()));
        }

        if (file != null) {
            for (String s : permissionRequest.getPermissiontypes()) {
                switch(s) {
                    case "read":
                        permissionService.removePermissionForUser(file, BasePermission.READ, authentication.getName());
                        break;
                    case "write":
                        permissionService.removePermissionForUser(file, BasePermission.WRITE, authentication.getName());
                        break;
                    case "admin":
                        permissionService.removePermissionForUser(file, BasePermission.ADMINISTRATION, authentication.getName());
                        break;
                    case "delete":
                        permissionService.removePermissionForUser(file, BasePermission.DELETE, authentication.getName());
                        break;
                }
            }
            return ResponseEntity.ok(new MessageResponse("Permissions successfully updated."));
        } else {
            return ResponseEntity.ok(new MessageResponse("File not found or you may not have permission to add users to this file. Please contact a moderator for more support."));
        }
    }

}
