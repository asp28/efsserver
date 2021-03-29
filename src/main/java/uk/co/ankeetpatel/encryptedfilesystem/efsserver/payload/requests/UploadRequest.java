package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

public class UploadRequest {
    @NotBlank
    private ArrayList<byte[]> bytes;

    @NotBlank
    private String fileName;

    @NotBlank
    private Long id;

    public ArrayList<byte[]> getBytes() {
        return bytes;
    }

    public void setBytes(ArrayList<byte[]> bytes) {
        this.bytes = bytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
