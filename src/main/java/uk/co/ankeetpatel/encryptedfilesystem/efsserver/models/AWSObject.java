package uk.co.ankeetpatel.encryptedfilesystem.efsserver.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * AWSObject held on S3 bucket
 */
public class AWSObject implements Serializable {

    private ArrayList<byte[]> file;

    private final String originalName;

    /**
     *
     * @param file
     * @param originalName
     */
    public AWSObject(ArrayList<byte[]> file, String originalName) {
        this.file = file;
        this.originalName = originalName;
    }

    /**
     *
     * @return ArrayList<byte[]>
     */
    public ArrayList<byte[]> getFile() {
        return file;
    }

    /**
     *
     * @param file
     */
    public void setFile(ArrayList<byte[]> file) {
        this.file = file;
    }

    /**
     *
     * @return String
     */
    public String getOriginalName() {
        return originalName;
    }

}
