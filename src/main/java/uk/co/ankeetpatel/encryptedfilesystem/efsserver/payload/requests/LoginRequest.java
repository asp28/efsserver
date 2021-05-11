package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;

import java.io.Serializable;

/**
 * Login request form
 */
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;

    /**
     * Default constructor
     */
    public LoginRequest()
    {
    }

    /**
     *
     * @param username
     * @param password
     */
    public LoginRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    /**
     *
     * @return String
     */
    public String getUsername() {
        return this.username;
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
     * @return String
     */
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
