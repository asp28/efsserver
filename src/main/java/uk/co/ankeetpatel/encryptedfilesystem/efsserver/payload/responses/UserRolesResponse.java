package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses;

import java.util.ArrayList;

public class UserRolesResponse {

    private ArrayList<String> roles;

    public UserRolesResponse(ArrayList<String> roles) {
        this.roles = roles;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }
}
