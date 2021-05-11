package uk.co.ankeetpatel.encryptedfilesystem.efsserver.models;


import org.springframework.security.core.GrantedAuthority;

/**
 * User Roles
 */
public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER, ROLE_MODERATOR, ROLE_SUPERADMIN;

    /**
     *
     * @return String
     */
    public String getAuthority() {
        return name();
    }

}