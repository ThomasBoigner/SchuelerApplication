package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.Grade;
import at.spengergasse.schuelerbackend.domain.Lesson;
import at.spengergasse.schuelerbackend.domain.Student;
import at.spengergasse.schuelerbackend.persistence.GradeRepository;
import at.spengergasse.schuelerbackend.persistence.LessonRepository;
import at.spengergasse.schuelerbackend.persistence.StudentRepository;
import at.spengergasse.schuelerbackend.service.dto.command.MutateGradeCommand;
import at.spengergasse.schuelerbackend.service.dto.command.UpdateGradeCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Slf4j
@Service
@Transactional(readOnly = true)
public class GradeService {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final TokenService tokenService;

    public List<Grade> getGrades(){
        log.debug("Trying to get all grades");
        List<Grade> allGrades = gradeRepository.findAll();
        log.info("Retrieved all ({}) grades", allGrades.size());
        return allGrades;
    }

    public Optional<Grade> getGrade(String token){
        log.debug("Trying to get grade with token {}", token);
        Optional<Grade> foundGrade = gradeRepository.findByToken(token);
        if (foundGrade.isPresent()){
            log.info("Found grade {} with token {}", foundGrade.get(), foundGrade.get().getToken());
        } else {
            log.info("No grade with token {} found", token);
        }
        return foundGrade;
    }

    @Transactional(readOnly = false)
    public Grade createGrade(MutateGradeCommand command){
        log.debug("Trying to create grade with command {}", command);
        Objects.requireNonNull(command, "Command must not be null");

        Grade createdGrade = _createGrade(Optional.empty(), command);
        log.info("Created grade {}", createdGrade);
        return createdGrade;
    }

    @Transactional(readOnly = false)
    public Grade replaceGrade(String token, MutateGradeCommand command){
        log.debug("Trying to replace grade with token {} with command {}", token, command);

        if (!gradeRepository.existsGradeByToken(token)){
            log.warn("Grade with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("grade with token %s can not be found!", token));
        }

        gradeRepository.deleteGradeByToken(token);
        log.trace("Deleted grade with token {}", token);

        Grade replacedGrade = _createGrade(Optional.of(token), command);
        log.info("Successfully replaced grade {}", replacedGrade);
        return replacedGrade;
    }

    @Transactional(readOnly = false)
    public Grade partiallyUpdateGrade(String token, UpdateGradeCommand command){
        log.debug("Trying to update grade with token {} with command {}", token, command);
        Objects.requireNonNull(command, "Command must not be null!");

        Optional<Grade> entity = gradeRepository.findByToken(token);

        if (entity.isEmpty()) {
            log.warn("Grade with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("Grade with token %s can not be found!", token));
        }

        Grade grade = entity.get();
        if (command.gradeValue() != null) grade.setGradeValue(command.gradeValue());
        if (command.student() != null) grade.setStudent(studentRepository.findStudentsByToken(command.student())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can not find student with token %s", command.student()))));
        if (command.lesson() != null) grade.setLesson(lessonRepository.findLessonByToken(command.lesson())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can not find lesson with token %s", command.lesson()))));

        log.info("Successfully updated grade {}", grade);
        return gradeRepository.save(grade);
    }

    @Transactional(readOnly = false)
    public void deleteGrades(){
        log.info("Successfully deleted all grades");
        gradeRepository.deleteAll();
    }

    @Transactional(readOnly = false)
    public void deleteGrade(String token){
        log.info("Successfully deleted grade with token {}", token);
        gradeRepository.deleteGradeByToken(token);
    }

    private Grade _createGrade(Optional<String> token, MutateGradeCommand command) {
        String tokenValue = token.orElseGet(tokenService::createNanoId);
        log.trace("Token value for new grade {}", tokenValue);

        Student student = studentRepository.findStudentsByToken(command.student())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can not find student with token %s", command.student())));
        log.trace("Retrieved student {} with token {} for initialization of the grade", student, token);

        Lesson lesson = lessonRepository.findLessonByToken(command.lesson())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can not find lesson with token %s", command.lesson())));
        log.trace("Retrieved lesson {} with token {} for initialization of the grade", lesson, token);

        Grade grade = Grade.builder()
                .gradeValue(command.gradeValue())
                .token(tokenValue)
                .student(student)
                .lesson(lesson)
                .build();

        log.trace("Mapped command {} to grade object {}", command, grade);
        return gradeRepository.save(grade);
    }
}
