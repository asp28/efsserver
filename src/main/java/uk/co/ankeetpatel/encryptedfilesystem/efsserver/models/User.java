package uk.co.ankeetpatel.encryptedfilesystem.efsserver.models;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User entity
 */
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "dateCreated")
    private Date dateCreated;

    @Column(name = "dateUpdated")
    private Date dateUpdated;

    @Column(name = "username")
    @Size(max = 20)
    private String username;

    @Column(name = "email")
    @Size(max = 50)
    private String email;

    @Column(name = "password")
    @Size(max = 120)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    List<Role> roles;

    /**
     * Default contructor
     */
    public User() {}

    /**
     *
     * @param name
     * @param username
     * @param email
     * @param password
     */
    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     *
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return Date
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     *
     * @param dateCreated
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     *
     * @return Date
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }

    /**
     *
     * @param dateUpdated
     */
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    /**
     *
     * @return List<Role>
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     *
     * @param roles
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
