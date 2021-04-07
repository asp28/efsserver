package uk.co.ankeetpatel.encryptedfilesystem.efsserver.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.Role;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.User;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests.RolesRequest;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests.SignupRequest;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses.MessageResponse;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.UserRepository;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.services.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @PreAuthorize("hasAnyRole('[moderator, admin, superadmin]')")
    @GetMapping("users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("add")
    public ResponseEntity<?> addUser(@RequestBody SignupRequest signupRequest) {
        if (userService.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signupRequest.getName(), signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRoles();
        List<Role> roles = new ArrayList<>();

        if (strRoles == null) {
            roles.add(Role.ROLE_USER);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        roles.add(Role.ROLE_ADMIN);
                        break;
                    case "user":
                        roles.add(Role.ROLE_USER);
                        break;
                    case "superadmin":
                        roles.add(Role.ROLE_SUPERADMIN);
                        break;
                    case "mod":
                        roles.add(Role.ROLE_MODERATOR);
                        break;
                }
            });
        }
        user.setRoles(roles);
        user.setDateCreated(new Date());
        this.userService.save(user);
        return ResponseEntity.ok(new MessageResponse("User added."));
    }

    @PostMapping("roles")
    public User editRole(@Valid @RequestBody RolesRequest rolesRequest) {
        User user = userService.findByUsername(rolesRequest.getUsername());
        for (Map.Entry s : rolesRequest.getRoles().entrySet()) {
            switch (s.getKey().toString()) {
                case "mod":
                    if (userService.hasRoleAdmin() || userService.hasRoleSuperAdmin()) {
                        if (s.getValue().toString().equals("false")) {
                            user.getRoles().remove(Role.ROLE_MODERATOR);
                        } else {
                            user.getRoles().add(Role.ROLE_MODERATOR);
                        }
                    }
                    break;
                case "admin":
                    if(userService.hasRoleSuperAdmin()) {
                        if (s.getValue().toString().equals("false")) {
                            user.getRoles().remove(Role.ROLE_ADMIN);
                        } else {
                            user.getRoles().add(Role.ROLE_ADMIN);
                        }
                    }
                    break;
                case "superadmin":
                    user.getRoles().add(Role.ROLE_SUPERADMIN);
                    break;
            }
        }
        userService.save(user);
        return user;
    }



}
