package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.*;
import at.spengergasse.schuelerbackend.domain.Class;
import at.spengergasse.schuelerbackend.foundation.TemporalValueFactory;
import at.spengergasse.schuelerbackend.persistence.*;
import at.spengergasse.schuelerbackend.service.dto.command.MutateStudentCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Slf4j
@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final GradeRepository gradeRepository;

    private final TemporalValueFactory temporalValueFactory;
    private final TokenService tokenService;

    public List<Student> getStudents(){
        log.debug("Trying to get all students");
        List<Student> allStudents = studentRepository.findAll();
        log.info("Retrieved all ({}) students", allStudents.size());
        return allStudents;
    }

    public List<Student> getStudentsByClass(Class _class){
        log.debug("Trying to get the students in the class {}", _class.getName());
        List<Student> studentsInClass = studentRepository.getBy_class(_class);
        log.info("Retrieved all ({}) students in class {}", studentsInClass.size(), _class.getName());
        return studentsInClass;
    }

    public Optional<Student> getStudent(String token){
        log.debug("Trying to get the student with token {}", token);
        Optional<Student> foundStudent = studentRepository.findStudentsByToken(token);
        if (foundStudent.isPresent()) {
            log.info("Found student {} with token {}", foundStudent.get(), foundStudent.get().getId());
        } else {
            log.info("No student with token {} found", token);
        }
        return foundStudent;
    }

    @Transactional(readOnly = false)
    public Student createStudent(MutateStudentCommand command){
        log.debug("Trying to create student with command {}", command);
        Objects.requireNonNull(command, "Command must not be null!");

        Student createdStudent = _createStudent(Optional.empty(), command);
        log.info("Created student {}", createdStudent);
        return createdStudent;
    }

    @Transactional(readOnly = false)
    public Student replaceStudent(String token, MutateStudentCommand command){
        log.debug("Trying to replace student with token {} with command {}", token, command);
        Objects.requireNonNull(command, "Command must not be null!");

        if (!studentRepository.existsStudentByToken(token)){
            log.warn("Student with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("Student with token %s can not be found!", token));
        }

        studentRepository.deleteStudentByToken(token);
        log.trace("Deleted student with token {}", token);

        Student replacedStudent = _createStudent(Optional.of(token), command);
        log.info("Successfully replaced student {}", replacedStudent);
        return replacedStudent;
    }

    @Transactional(readOnly = false)
    public Student partiallyUpdateStudent(String token, MutateStudentCommand command){
        log.debug("Trying to update student with token {} with command {}", token, command);
        Objects.requireNonNull(command, "Command must not be null!");

        Optional<Student> entity = studentRepository.findStudentsByToken(token);

        if (entity.isEmpty()){
            log.warn("Student with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("Student with token %s can not be found!", token));
        }

        Student student = entity.get();
        if (command.firstname() != null) student.setFirstname(command.firstname());
        if (command.lastname() != null) student.setLastname(command.lastname());
        if (command.email() != null) student.setEmail(command.email());
        if (command._class() != null) student.set_class(classRepository.getClassByToken(command._class()).orElseThrow(() -> new IllegalArgumentException(String.format("Class with token %s can not be found!", command._class()))));
        if (command.conferenceDecision() != null) student.setConferenceDecision(command.conferenceDecision());

        log.info("Successfully updated student {}", student);
        return studentRepository.save(student);
    }

    @Transactional(readOnly = false)
    public void deleteStudents(){
        log.info("Successfully deleted all classes");
        studentRepository.deleteAll();
    }

    @Transactional(readOnly = false)
    public void deleteStudent(String token){
        log.info("Successfully deleted class with token {}", token);
        studentRepository.deleteStudentByToken(token);
    }

    private Student _createStudent(Optional<String> token, MutateStudentCommand command){
        String tokenValue = token.orElseGet(tokenService::createNanoId);
        log.trace("Token value for new class {}", tokenValue);

        Class _class = classRepository.getClassByToken(command._class()).orElse(null);
        log.trace("Retrieved class {} with token {} for initialization of the student", _class, token);

        Student student = Student.builder()
                .firstname(command.firstname())
                .lastname(command.lastname())
                .email(command.email())
                .conferenceDecision(command.conferenceDecision())
                ._class(_class)
                .token(tokenValue)
                .grades(new ArrayList<>())
                .build();

        log.trace("Mapped command {} to class object {}", command, _class);
        return studentRepository.save(student);
    }
}
