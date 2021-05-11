package uk.co.ankeetpatel.encryptedfilesystem.efsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;

import java.util.List;
import java.util.Optional;


@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    /**
     *
     * @return List<File>
     */
    List<File> findAll();

    /**
     *
     * @param id
     * @return Optional<File>
     */
    Optional<File> findById(Long id);

}
