package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses;

public class FileUploadResponse {

    private String publicKey;
    private Long id;

    public FileUploadResponse(Long id, String publicKey) {
        this.id = id;
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
