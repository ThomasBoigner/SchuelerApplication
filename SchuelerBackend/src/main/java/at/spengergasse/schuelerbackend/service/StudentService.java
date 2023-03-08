package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.*;
import at.spengergasse.schuelerbackend.domain.Class;
import at.spengergasse.schuelerbackend.foundation.TemporalValueFactory;
import at.spengergasse.schuelerbackend.persistence.*;
import at.spengergasse.schuelerbackend.service.dto.command.MutateStudentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final GradeRepository gradeRepository;
    private final LessonRepository lessonRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    private final TemporalValueFactory temporalValueFactory;
    private final TokenService tokenService;

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public List<Student> getStudentsByClass(Class _class){
        return studentRepository.getBy_class(_class);
    }

    public Optional<Student> getStudent(String id){
        return studentRepository.findStudentsByToken(id);
    }

    public Student createStudent(MutateStudentCommand command){
        Objects.requireNonNull(command, "Command must not be null!");

        Class _class = classRepository.getByToken(command._class()).orElse(null);

        Student student = Student.builder()
                .firstname(command.firstname())
                .lastname(command.lastname())
                .email(command.email())
                .conferenceDecision(command.conferenceDecision())
                ._class(_class)
                .token(tokenService.createNanoId())
                .creationTS(temporalValueFactory.now())
                .build();

        student.setGrades(command.grades().stream().map((String token) -> gradeRepository.findByToken(token).orElse(null)).toList());

        return studentRepository.save(student);
    }
}
