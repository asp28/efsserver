package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private String token;
    private String type = "Bearer";
    private String username;
    private Collection<? extends GrantedAuthority> roles;

    public JwtResponse(String accessToken, String username, Collection<? extends GrantedAuthority> roles) {
        this.token = accessToken;
        this.username = username;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles;
    }
}