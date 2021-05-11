package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;

import javax.validation.constraints.NotBlank;

/**
 * Download Request class
 */
public class DownloadRequest {

    @NotBlank
    private Long id;

    @NotBlank
    private String publicKey;

    /**
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return String
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     *
     * @param publicKey
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
