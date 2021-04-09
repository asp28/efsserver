package uk.co.ankeetpatel.encryptedfilesystem.efsserver.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.Exception.NoAccessToFileException;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.FileRepository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    public File returnFileIfTheyCanRead(long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        if (optionalFile.isPresent()) {
            return optionalFile.get();
        }else {
            return null;
        }
    }

    @PostAuthorize("hasPermission(returnObject, 'WRITE')")
    public File returnFileIfTheyCanWrite(long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        if (optionalFile.isPresent()) {
            return optionalFile.get();
        }else {
            return null;
        }
    }

    @PostAuthorize("hasPermission(returnObject, 'DELETE')")
    public File returnFileIfTheyCanDelete(long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        if (optionalFile.isPresent()) {
            return optionalFile.get();
        }else {
            return null;
        }
    }

    @PostAuthorize("hasPermission(returnObject, 'ADMINISTRATION')")
    public File returnFileIfTheyCanAdmin(long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        if (optionalFile.isPresent()) {
            return optionalFile.get();
        }else {
            return null;
        }
    }


    @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, 'write') or hasPermission(filterObject, 'administration') or hasPermission(filterObject, 'delete')")
    public List<File> getAll() {
        return fileRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('[ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN, ROLE_SUPERADMIN]')")
    public void save(File file) {
        fileRepository.save(file);
    }

    @PostAuthorize("hasPermission(returnObject, 'read')")
    public File findById(long id) throws NoAccessToFileException {
        Optional<File> file = fileRepository.findById(id);
        if (file.isPresent()) {
            return fileRepository.findById(id).get();
        }
        throw new NoAccessToFileException();
    }

    @PreAuthorize("hasPermission(#file, 'delete')")
    public void delete(File file) {
        fileRepository.delete(file);
    }


}
