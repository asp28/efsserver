package uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     *
     * @param username
     * @return User
     */
    User findByUsername(String username);

    /**
     *
     * @param username
     * @return boolean
     */
    boolean existsByUsername(String username);

    /**
     *
     * @param email
     * @return boolean
     */
    boolean existsByEmail(String email);
}
