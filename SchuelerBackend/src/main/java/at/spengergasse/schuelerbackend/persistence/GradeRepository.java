package at.spengergasse.schuelerbackend.persistence;

import at.spengergasse.schuelerbackend.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByToken(String token);
}
