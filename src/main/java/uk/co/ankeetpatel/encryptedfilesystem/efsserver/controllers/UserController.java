package uk.co.ankeetpatel.encryptedfilesystem.efsserver.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.User;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("users/add")
    public User addUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }

}
