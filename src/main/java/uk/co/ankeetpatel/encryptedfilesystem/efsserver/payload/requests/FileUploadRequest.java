package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;



import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;

import javax.validation.constraints.NotBlank;

public class FileUploadRequest {

    @NotBlank
    private String fileName;

    private File parentFile;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getParentFile() {
        return parentFile;
    }

    public void setParentFile(File parentFile) {
        this.parentFile = parentFile;
    }
}
