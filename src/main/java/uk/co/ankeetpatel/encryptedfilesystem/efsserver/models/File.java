package uk.co.ankeetpatel.encryptedfilesystem.efsserver.models;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

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

    public File(String fileName) {

        this.fileName = fileName;
    }

    public File() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(Boolean fileUploaded) {
        this.fileUploaded = fileUploaded;
    }
}
