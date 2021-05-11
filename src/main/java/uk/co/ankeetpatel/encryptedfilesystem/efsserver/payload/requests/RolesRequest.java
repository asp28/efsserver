package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;

import java.util.HashMap;

public class RolesRequest {

    private String username;

    private HashMap<String, String> roles;

    /**
     * Constructor
     * @param username
     * @param roles
     */
    public RolesRequest(String username, HashMap<String, String> roles) {
        this.username = username;
        this.roles = roles;
    }

    /**
     *
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getRoles() {
        return roles;
    }

    /**
     *
     * @param roles
     */
    public void setRoles(HashMap<String, String> roles) {
        this.roles = roles;
    }
}
