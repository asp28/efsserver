package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;

import java.util.ArrayList;
import java.util.HashMap;

public class PermissionRequest {

    private Long fileID;

    private HashMap<String, String> permissiontypes;

    private Long userID;

    /**
     *
     * @return Long
     */
    public Long getFileID() {
        return fileID;
    }

    /**
     *
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getPermissiontypes() {
        return permissiontypes;
    }

    /**
     *
     * @return Long
     */
    public Long getUserID() {
        return userID;
    }

}
