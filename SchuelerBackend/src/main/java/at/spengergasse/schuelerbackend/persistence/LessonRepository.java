package at.spengergasse.schuelerbackend.persistence;

import at.spengergasse.schuelerbackend.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> getLessonBy_class_NameAndTeacher_LastnameAndSubject_Longname(String className, String teacherLastName, String subjectName);
}
