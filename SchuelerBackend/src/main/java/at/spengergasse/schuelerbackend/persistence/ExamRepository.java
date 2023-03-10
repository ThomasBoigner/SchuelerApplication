package at.spengergasse.schuelerbackend.persistence;

import at.spengergasse.schuelerbackend.domain.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    boolean existsExamByDate(LocalDateTime date);
    Optional<Exam> getExamByToken(String token);
    boolean existsExamByToken(String token);
    void deleteExamByToken(String token);
}
