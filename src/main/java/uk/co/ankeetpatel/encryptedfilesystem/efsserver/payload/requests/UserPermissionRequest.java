package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;

import javax.validation.constraints.NotBlank;

public class UserPermissionRequest {

    @NotBlank
    private String username;

    @NotBlank
    private long fileID;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getFileID() {
        return fileID;
    }

    public void setFileID(long fileID) {
        this.fileID = fileID;
    }
}
