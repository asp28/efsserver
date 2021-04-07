package uk.co.ankeetpatel.encryptedfilesystem.efsserver.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.User;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasAnyRole('[ROLE_MODERATOR, ROLE_ADMIN, ROLE_SUPERADMIN]')")
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @PreAuthorize("hasAnyRole('[ROLE_MODERATOR, ROLE_ADMIN, ROLE_SUPERADMIN]')")
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @PreAuthorize("hasAnyRole('[ROLE_MODERATOR, ROLE_ADMIN, ROLE_SUPERADMIN]')")
    public boolean existsByEmail(String Email) {
        return userRepository.existsByEmail(Email);
    }

    @PreAuthorize("hasAnyRole('[ROLE_MODERATOR, ROLE_ADMIN, ROLE_SUPERADMIN]')")
    public void save(User user) {
        userRepository.save(user);
    }

    @PreAuthorize("hasAnyRole('[ROLE_ADMIN, ROLE_SUPERADMIN]')")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public boolean hasRoleMod() {
        return true;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean hasRoleAdmin() {
        return true;
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public boolean hasRoleSuperAdmin() {
        return true;
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

}
