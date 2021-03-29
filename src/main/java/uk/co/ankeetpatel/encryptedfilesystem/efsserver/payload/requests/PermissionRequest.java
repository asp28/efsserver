package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;

import java.util.ArrayList;

public class PermissionRequest {

    private Long fileID;

    private ArrayList<String> permissiontypes;

    private Long userID;

    public Long getFileID() {
        return fileID;
    }

    public void setFileID(Long fileID) {
        this.fileID = fileID;
    }

    public ArrayList<String> getPermissiontypes() {
        return permissiontypes;
    }

    public void setPermissiontypes(ArrayList<String> permissiontypes) {
        this.permissiontypes = permissiontypes;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
