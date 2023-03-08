package at.spengergasse.schuelerbackend.persistence;

import at.spengergasse.schuelerbackend.domain.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    Optional<Class> getByToken(String token);
    Optional<Class> getByName(String name);
}
