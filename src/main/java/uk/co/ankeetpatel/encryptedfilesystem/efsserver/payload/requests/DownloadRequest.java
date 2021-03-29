package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;

import javax.validation.constraints.NotBlank;

public class DownloadRequest {

    @NotBlank
    private Long id;

    @NotBlank
    private String publicKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
