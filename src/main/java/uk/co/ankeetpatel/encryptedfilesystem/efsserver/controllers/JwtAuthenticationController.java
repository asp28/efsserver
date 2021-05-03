package uk.co.ankeetpatel.encryptedfilesystem.efsserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.Role;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.User;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests.LoginRequest;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.requests.SignupRequest;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses.JwtResponse;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses.MessageResponse;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.UserRepository;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.security.jwt.JwtTokenUtil;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.security.jwt.JwtUserDetailsService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(new MessageResponse("WORKING"));
    }


    @RequestMapping(value = "signin", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());


        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token,
                userDetails.getUsername(),
                userDetails.getAuthorities()));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
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
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User successfully created."));
    }

}
