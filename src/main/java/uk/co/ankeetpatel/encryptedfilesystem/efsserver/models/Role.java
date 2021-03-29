package uk.co.ankeetpatel.encryptedfilesystem.efsserver.models;


import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER, ROLE_MODERATOR, ROLE_SUPERADMIN;

    public String getAuthority() {
        return name();
    }

}