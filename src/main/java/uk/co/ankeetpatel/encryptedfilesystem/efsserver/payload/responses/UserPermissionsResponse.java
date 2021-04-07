package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses;

import java.util.List;

public class UserPermissionsResponse {

    private String username;

    private List<Integer> permissionValues;

    private String filename;

    private long fileID;

    private long userID;

    public UserPermissionsResponse(String username, List<Integer> permissionValues, String filename, long fileID, long userID) {
        this.username = username;
        this.permissionValues = permissionValues;
        this.filename = filename;
        this.fileID = fileID;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getPermissionValues() {
        return permissionValues;
    }

    public void setPermissionValues(List<Integer> permissionValues) {
        this.permissionValues = permissionValues;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFileID() {
        return fileID;
    }

    public void setFileID(long fileID) {
        this.fileID = fileID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
