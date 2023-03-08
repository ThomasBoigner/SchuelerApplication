package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.*;
import at.spengergasse.schuelerbackend.domain.Class;
import at.spengergasse.schuelerbackend.foundation.TemporalValueFactory;
import at.spengergasse.schuelerbackend.persistence.*;
import at.spengergasse.schuelerbackend.service.dto.command.MutateStudentCommand;
import at.spengergasse.schuelerbackend.service.dto.command.UpdateStudentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final GradeRepository gradeRepository;

    private final TemporalValueFactory temporalValueFactory;
    private final TokenService tokenService;

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public List<Student> getStudentsByClass(Class _class){
        return studentRepository.getBy_class(_class);
    }

    public Optional<Student> getStudent(String token){
        return studentRepository.findStudentsByToken(token);
    }

    @Transactional(readOnly = false)
    public Student createStudent(MutateStudentCommand command){
        Objects.requireNonNull(command, "Command must not be null!");

        return _createStudent(Optional.empty(), command);
    }

    @Transactional(readOnly = false)
    public Student replaceStudent(String token, MutateStudentCommand command){
        Objects.requireNonNull(command, "Command must not be null!");

        if (!studentRepository.existsStudentByToken(token)){
            throw new IllegalArgumentException(String.format("Student with token %s can not be found!", token));
        }

        studentRepository.deleteStudentByToken(token);
        return _createStudent(Optional.of(token), command);
    }

    @Transactional(readOnly = false)
    public Student partiallyUpdateStudent(String token, UpdateStudentCommand command){
        Objects.requireNonNull(command, "Command must not be null!");

        Optional<Student> entity = studentRepository.findStudentsByToken(token);

        if (!entity.isPresent()){
            throw new IllegalArgumentException(String.format("Student with token %s can not be found!", token));
        }

        Student student = entity.get();
        if (command.firstname() != null && !command.firstname().isBlank()) student.setFirstname(command.firstname());
        if (command.lastname() != null && !command.lastname().isBlank()) student.setLastname(command.lastname());
        if (command.email() != null && !command.email().isBlank()) student.setEmail(command.email());
        if (command._class() != null) student.set_class(classRepository.getClassByToken(command._class()).orElseThrow(() -> new IllegalArgumentException(String.format("Class with token %s can not be found!", command._class()))));
        if (command.conferenceDecision() != null) student.setConferenceDecision(command.conferenceDecision().booleanValue());
        if (command.grades() != null && !command.grades().isEmpty()) student.setGrades(command.grades().stream().map((String gradeToken) -> gradeRepository.findByToken(gradeToken).orElse(null)).toList());

        return studentRepository.save(student);
    }

    @Transactional(readOnly = false)
    public void deleteStudents(){
        studentRepository.deleteAll();
    }

    @Transactional(readOnly = false)
    public void deleteStudent(String token){
        studentRepository.deleteStudentByToken(token);
    }

    private Student _createStudent(Optional<String> token, MutateStudentCommand command){
        LocalDateTime creationTS = temporalValueFactory.now();
        String tokenValue = token.orElseGet(() -> tokenService.createNanoId());

        Class _class = classRepository.getClassByToken(command._class()).orElse(null);

        Student student = Student.builder()
                .firstname(command.firstname())
                .lastname(command.lastname())
                .email(command.email())
                .conferenceDecision(command.conferenceDecision())
                ._class(_class)
                .token(tokenValue)
                .creationTS(creationTS)
                .build();

        student.setGrades(command.grades().stream().map((String gradeToken) -> gradeRepository.findByToken(gradeToken).orElse(null)).toList());

        return studentRepository.save(student);
    }
}
