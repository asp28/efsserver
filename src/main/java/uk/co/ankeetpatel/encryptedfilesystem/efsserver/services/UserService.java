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

    /**
     * Find user by username
     * @param username
     * @return User
     */
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Check if user exists by username
     * @param username
     * @return Boolean
     */
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check user exists by email
     * @param Email
     * @return Boolean
     */
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public boolean existsByEmail(String Email) {
        return userRepository.existsByEmail(Email);
    }

    /**
     * Save user
     * @param user
     */
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Find All Users
     * @return List<User>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * If user has Mod
     * @return Boolean
     */
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public boolean hasRoleMod() {
        return true;
    }

    /**
     * If user has Admin
     * @return Boolean
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean hasRoleAdmin() {
        return true;
    }

    /**
     * If user has SuperAdmin
     * @return Boolean
     */
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public boolean hasRoleSuperAdmin() {
        return true;
    }

    /**
     * Find user by id
     * @param id
     * @return Optional<User>
     */
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    /**
     * Get user by ID
     * @param id
     * @return User
     */
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public User getById(long id) {
        return userRepository.getOne(id);
    }

}
