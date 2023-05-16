package at.spengergasse.schuelerbackend.persistence;

import at.spengergasse.schuelerbackend.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> getByLastnameAndFirstname(String Lastname, String Firstname);
    Optional<Teacher> findByToken(String token);
}
