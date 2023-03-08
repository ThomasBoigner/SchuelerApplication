package at.spengergasse.schuelerbackend.persistence;

import at.spengergasse.schuelerbackend.domain.Class;
import at.spengergasse.schuelerbackend.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> getBy_class(Class _class);
    Optional<Student> findStudentsByToken(String token);
    boolean existsStudentByToken(String token);
    void deleteStudentByToken(String token);
}
