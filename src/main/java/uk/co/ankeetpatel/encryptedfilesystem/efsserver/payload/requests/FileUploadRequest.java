package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;



import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;

import javax.validation.constraints.NotBlank;

/**
 * File upload request
 */
public class FileUploadRequest {

    @NotBlank
    private String fileName;

    private File parentFile;

    /**
     *
     * @return String
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     *
     * @return File
     */
    public File getParentFile() {
        return parentFile;
    }

    /**
     *
     * @param parentFile
     */
    public void setParentFile(File parentFile) {
        this.parentFile = parentFile;
    }
}
