package uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
