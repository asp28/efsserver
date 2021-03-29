package uk.co.ankeetpatel.encryptedfilesystem.efsserver.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.Event;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.EventEnum;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.Role;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.User;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.EventRepository;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.FileRepository;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*
@Component
public class DbSeeder implements CommandLineRunner {
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private FileRepository fileRepository;

    public DbSeeder(UserRepository userRepository, EventRepository eventRepository, FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        User ankeet = new User(
                "ankeet patel",
                "ankeet",
                "ankeet@me.com",
                "$2a$10$d1ov3Bt2y8vMd5njYWEqv.bKD039QyGBDNRhiKP2553BFmPrtjmDK"
        );
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_USER);

        ankeet.setDateCreated(new Date());

        Event ankeetpatel = new Event(ankeet, EventEnum.USER_CREATED);

        User aiden = new User(
                "aiden perry",
                "aiden",
                "aiden@me.com",
                "$2a$10$d1ov3Bt2y8vMd5njYWEqv.bKD039QyGBDNRhiKP2553BFmPrtjmDK"
        );

        aiden.setDateCreated(new Date());


        Event aidenperry = new Event(aiden, EventEnum.USER_CREATED);

        User chris = new User(
                "chris saraty",
                "chris",
                "chris@me.com",
                "$2a$10$d1ov3Bt2y8vMd5njYWEqv.bKD039QyGBDNRhiKP2553BFmPrtjmDK"
        );

        chris.setDateCreated(new Date());

        Event chrissaraty = new Event(chris, EventEnum.USER_CREATED);

        ankeet.setRoles(roles);
        roles.add(Role.ROLE_MODERATOR);
        aiden.setRoles(roles);
        roles.add(Role.ROLE_ADMIN);
        chris.setRoles(roles);


        // drop all hotels
        this.userRepository.deleteAll();
        this.eventRepository.deleteAll();
        this.fileRepository.deleteAll();

        this.userRepository.save(ankeet);
        this.userRepository.save(aiden);
        this.userRepository.save(chris);
        this.eventRepository.save(ankeetpatel);
        this.eventRepository.save(aidenperry);
        this.eventRepository.save(chrissaraty);
    }
}
*/