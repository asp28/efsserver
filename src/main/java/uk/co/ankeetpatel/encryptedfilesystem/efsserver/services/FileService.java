package uk.co.ankeetpatel.encryptedfilesystem.efsserver.services;


import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.FileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    final FileRepository fileRepository;

    /**
     * Constructor
     * @param fileRepository
     */
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Return file if the user has read permissions
     * @param id
     * @return File
     */
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    public File returnFileIfTheyCanRead(long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        return optionalFile.orElse(null);
    }

    /**
     * Return file if the user has write permissions
     * @param id
     * @return File
     */
    @PostAuthorize("hasPermission(returnObject, 'WRITE')")
    public File returnFileIfTheyCanWrite(long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        return optionalFile.orElse(null);
    }

    /**
     * Return file if the user has admin permissions
     * @param id
     * @return File
     */
    @PostAuthorize("hasPermission(returnObject, 'ADMINISTRATION')")
    public File returnFileIfTheyCanAdmin(long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        return optionalFile.orElse(null);
    }

    /**
     * Return all files the user can R/W/A/D
     * @return List<File>
     */
    @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, 'write') or hasPermission(filterObject, 'administration') or hasPermission(filterObject, 'delete')")
    public List<File> getAll() {
        return fileRepository.findAll();
    }

    /**
     * Save file
     * @param file
     */
    public void save(File file) {
        fileRepository.save(file);
    }

    /**
     * Return file if user can read or admin
     * @param id
     * @return File
     */
    @PostAuthorize("hasPermission(returnObject, 'read') or hasPermission(returnObject, 'administration') ")
    public File findById(long id) {
        Optional<File> file = fileRepository.findById(id);
        return file.orElse(null);
    }

    /**
     * Delete file if user has delete perms for file
     * @param file
     */
    @PreAuthorize("hasPermission(#file, 'delete')")
    public void delete(File file) {
        fileRepository.delete(file);
    }


}
