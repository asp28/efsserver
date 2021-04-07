package uk.co.ankeetpatel.encryptedfilesystem.efsserver.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.Exception.NoAccessToFileException;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.FileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    @PreAuthorize("hasPermission(returnObject, 'read')")
    public File returnFileIfTheyCanRead(long id) {
        return fileRepository.getOne(id);
    }

    @PreAuthorize("hasPermission(returnObject, 'write')")
    public File returnFileIfTheyCanWrite(long id) {
        return fileRepository.getOne(id);
    }

    @PreAuthorize("hasPermission(returnObject, 'delete')")
    public File returnFileIfTheyCanDelete(long id) {
        return fileRepository.getOne(id);
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


    @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, 'write')")
    public List<File> getAll() {
        return fileRepository.findAll();
    }

    public void save(File file) {
        fileRepository.save(file);
    }

    @PreAuthorize("hasPermission(returnObject.get(), 'read')")
    public Optional<File> findById(long id) {
        return fileRepository.findById(id);
    }

    @PreAuthorize("hasPermission(#file, 'delete')")
    public void delete(File file) {
        fileRepository.delete(file);
    }


}
