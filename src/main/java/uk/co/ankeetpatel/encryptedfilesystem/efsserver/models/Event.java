package uk.co.ankeetpatel.encryptedfilesystem.efsserver.models;



import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @Column(name = "date")
    private Date date;

    @Column(name = "event")
    private EventEnum event;

    public Event() {}

    public Event(User user, EventEnum event) {
        this.user = user;
        this.event = event;
        date = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EventEnum getEvent() {
        return event;
    }

    public void setEvent(EventEnum event) {
        this.event = event;
    }
}
