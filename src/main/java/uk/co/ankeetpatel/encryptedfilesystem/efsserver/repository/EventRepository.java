package uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
