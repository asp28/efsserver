package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;

import javax.validation.constraints.NotBlank;

public class ChangePasswordRequest {

    @NotBlank
    private Long id;

    @NotBlank
    private String newPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
