package uk.co.ankeetpatel.encryptedfilesystem.efsserver.models;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/**
 * File entity
 */
@JsonIgnoreProperties(value = {"publicKey", "privateKey"})
@Entity
@Table(name = "Files")
public class File {

    @Id
    @SequenceGenerator(name = "Files_id_seq", sequenceName = "Files_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "dateUploaded")
    private Date dateUploaded;

    @Column(name = "dateModified")
    private Date dateModified;

    @Column(name = "publicKey", columnDefinition = "TEXT")
    private String publicKey;

    @Column(name = "privateKey", columnDefinition = "TEXT")
    private String privateKey;

    @Column(name = "fileUploaded")
    private Boolean fileUploaded;

    /**
     *
     * @param fileName
     */
    public File(String fileName) {

        this.fileName = fileName;
    }

    /**
     * Default constructor
     */
    public File() {
    }

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
     * @return Date
     */
    public Date getDateUploaded() {
        return dateUploaded;
    }

    /**
     *
     * @param dateUploaded
     */
    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    /**
     *
     * @return Date
     */
    public Date getDateModified() {
        return dateModified;
    }

    /**
     *
     * @param dateModified
     */
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
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

    /**
     *
     * @return String
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     *
     * @param privateKey
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     *
     * @return String
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     *
     * @return Boolean
     */
    public Boolean getFileUploaded() {
        return fileUploaded;
    }

    /**
     *
     * @param fileUploaded
     */
    public void setFileUploaded(Boolean fileUploaded) {
        this.fileUploaded = fileUploaded;
    }
}
