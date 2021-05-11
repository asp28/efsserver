package uk.co.ankeetpatel.encryptedfilesystem.efsserver.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * AWS S3 Object to upload
 */
public class AWSObject implements Serializable {

    private ArrayList<byte[]> file;

    private String originalName;

    /**
     * Contructor
     * @param file
     * @param originalName
     */
    public AWSObject(ArrayList<byte[]> file, String originalName) {
        this.file = file;
        this.originalName = originalName;
    }

    public ArrayList<byte[]> getFile() {
        return file;
    }

    public void setFile(ArrayList<byte[]> file) {
        this.file = file;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
