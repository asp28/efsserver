package uk.co.ankeetpatel.encryptedfilesystem.efsserver.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.User;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests.*;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.services.LocalPermissionService;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.Exception.NoAccessToFileException;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.algorithms.CipherUtility;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.AWSObject;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses.*;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.services.S3Services;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.services.FileService;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.services.UserService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    UserService userService;

    @Autowired
    CipherUtility cipherUtility;

    @Autowired
    S3Services s3Services;

    @Autowired
    LocalPermissionService permissionService;


    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<?> allAccess(Authentication authentication) {
        List<File> files = fileService.getAll();
        ArrayList<FilesResponse> responses = new ArrayList<>();
        for (File f : files) {
            responses.add(new FilesResponse(f, (ArrayList<Integer>) permissionService.getPermissions(f, authentication)));
        }

        return ResponseEntity.ok(new AllowedFilesResponse(responses));
    }

    //Request key works - need to make the key dynamic...generate key and send
    @PostMapping("/upload/request")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPERADMIN')")
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
            fileService.save(file);

            permissionService.updatePermissionForUser(file, BasePermission.READ, authentication.getName(), true);
            permissionService.updatePermissionForUser(file, BasePermission.WRITE, authentication.getName(), true);
            permissionService.updatePermissionForUser(file, BasePermission.ADMINISTRATION, authentication.getName(), true);

            permissionService.updatePermissionForAuthority(file, BasePermission.ADMINISTRATION, "ROLE_MODERATOR", true);
            permissionService.updatePermissionForAuthority(file, BasePermission.ADMINISTRATION, "ROLE_ADMIN", true);
            permissionService.updatePermissionForAuthority(file, BasePermission.READ, "ROLE_ADMIN", true);
            permissionService.updatePermissionForAuthority(file, BasePermission.WRITE, "ROLE_ADMIN", true);
            permissionService.updatePermissionForAuthority(file, BasePermission.DELETE, "ROLE_ADMIN", true);
            permissionService.updatePermissionForAuthority(file, BasePermission.ADMINISTRATION, "ROLE_SUPERADMIN", true);
            permissionService.updatePermissionForAuthority(file, BasePermission.READ, "ROLE_SUPERADMIN", true);
            permissionService.updatePermissionForAuthority(file, BasePermission.WRITE, "ROLE_SUPERADMIN", true);
            permissionService.updatePermissionForAuthority(file, BasePermission.DELETE, "ROLE_SUPERADMIN", true);

            return ResponseEntity.ok(new FileUploadResponse(file.getId(), file.getPublicKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(new MessageResponse("Error: Could not generate new set of keys."));
    }


    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('[USER, MODERATOR, ADMIN, SUPERADMIN]')")
    public ResponseEntity<?> uploadFile(@Valid @RequestBody UploadRequest UploadRequest) {
        File chosenFile;
        chosenFile = fileService.returnFileIfTheyCanWrite(UploadRequest.getId());

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
                fileService.save(chosenFile);
                return ResponseEntity.ok(new MessageResponse("File upload successful."));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return ResponseEntity.ok(new MessageResponse("Error: Invalid file id"));
        }

        return ResponseEntity.ok(new MessageResponse("Error uploading file. Try again."));
    }

    @PostMapping("/download")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<?> downloadFile(@Valid @RequestBody DownloadRequest downloadRequest) {
        File chosenFile;
        chosenFile = fileService.returnFileIfTheyCanRead(downloadRequest.getId());

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
            } catch (IOException | ClassNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok(new MessageResponse("File downloaded."));
        } else {
            return ResponseEntity.ok(new MessageResponse("File ID not found."));
        }
        //return ResponseEntity.ok(new MessageResponse("Error: Download request corrupted."));
    }

    @PostMapping("/updatepermission")
    @PreAuthorize("hasAnyRole('[USER, MODERATOR, ADMIN, SUPERADMIN]')")
    public ResponseEntity<?> addPermission(@Valid @RequestBody PermissionRequest permissionRequest, Authentication authentication) {

        File file = fileService.returnFileIfTheyCanAdmin(permissionRequest.getFileID());

        String username = userService.findById(permissionRequest.getUserID()).get().getUsername();

        for (Map.Entry s : permissionRequest.getPermissiontypes().entrySet()) {
            switch (s.getKey().toString()) {
                case "read":
                    permissionService.updatePermissionForUser(file, BasePermission.READ, username, Boolean.parseBoolean(s.getValue().toString()));
                    break;
                case "write":
                    permissionService.updatePermissionForUser(file, BasePermission.WRITE, username, Boolean.parseBoolean(s.getValue().toString()));
                    break;
                case "admin":
                    permissionService.updatePermissionForUser(file, BasePermission.ADMINISTRATION, username, Boolean.parseBoolean(s.getValue().toString()));
                    break;
                case "delete":
                    permissionService.updatePermissionForUser(file, BasePermission.DELETE, username, Boolean.parseBoolean(s.getValue().toString()));
                    break;
            }
        }
        return ResponseEntity.ok(new MessageResponse("Permissions successfully updated."));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getFile(@PathVariable long id, Authentication authentication) throws NoAccessToFileException {
        File file = fileService.returnFileIfTheyCanRead(id);
        ArrayList<Integer> allowedPermissions = (ArrayList<Integer>) permissionService.getPermissions(fileService.findById(id).get(), authentication);
        FilesResponse response = new FilesResponse(file, allowedPermissions);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable("id") long id) {
        Optional<File> file = fileService.findById(id);
        if (file.isPresent()) {
            fileService.delete(file.get());
            return ResponseEntity.ok(new MessageResponse("File deleted."));
        }
        return ResponseEntity.ok(new MessageResponse("File not found."));
    }

    @PostMapping("userpermissions")
    public ResponseEntity<?> getUserPermissions(@Valid @RequestBody UserPermissionRequest userPermissionRequest, Authentication authentication) {
        User user = userService.findByUsername(userPermissionRequest.getUsername());
        File file = fileService.returnFileIfTheyCanAdmin(userPermissionRequest.getFileID());
        List<Integer> permissions = permissionService.getPermissionsForUser(file, authentication, user);
        return ResponseEntity.ok(new UserPermissionsResponse(user.getUsername(), permissions, file.getFileName(), file.getId(), user.getId()));

    }


}
