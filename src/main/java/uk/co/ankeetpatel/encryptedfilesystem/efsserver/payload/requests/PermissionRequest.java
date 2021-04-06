package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;

import java.util.ArrayList;
import java.util.HashMap;

public class PermissionRequest {

    private Long fileID;

    private HashMap<String, String> permissiontypes;

    private Long userID;

    public Long getFileID() {
        return fileID;
    }

    public void setFileID(Long fileID) {
        this.fileID = fileID;
    }

    public HashMap<String, String> getPermissiontypes() {
        return permissiontypes;
    }

    public void setPermissiontypes(HashMap<String, String> permissiontypes) {
        this.permissiontypes = permissiontypes;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
