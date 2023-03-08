package at.spengergasse.schuelerbackend.persistence;

import at.spengergasse.schuelerbackend.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByShortname(String shortname);
    Optional<Subject> getByLongname(String longname);
}
