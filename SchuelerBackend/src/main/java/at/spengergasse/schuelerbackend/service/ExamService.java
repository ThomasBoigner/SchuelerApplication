package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.Exam;
import at.spengergasse.schuelerbackend.foundation.TemporalValueFactory;
import at.spengergasse.schuelerbackend.persistence.ExamRepository;
import at.spengergasse.schuelerbackend.persistence.GradeRepository;
import at.spengergasse.schuelerbackend.service.dto.command.MutateExamCommand;
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
public class ExamService {
    private final ExamRepository examRepository;
    private final GradeRepository gradeRepository;


    private final TemporalValueFactory temporalValueFactory;
    private final TokenService tokenService;

    public List<Exam> getExams(){
        log.debug("Trying to get all exams");
        List<Exam> allExams = examRepository.findAll();
        log.info("Retrieved all ({}) exams", allExams.size());
        return allExams;
    }

    public Optional<Exam> getExam(String token){
        log.debug("Trying to get the exam with token {}", token);
        Optional<Exam> foundExam = examRepository.getExamByToken(token);
        if (foundExam.isPresent()){
            log.info("Found exam {} with token {}", foundExam.get(), foundExam.get().getId());
        } else {
            log.info("No exam with token {} found", token);
        }
        return foundExam;
    }

    @Transactional(readOnly = false)
    public Exam createExam(MutateExamCommand command) {
        log.debug("Trying to create exam with command {}", command);
        Objects.requireNonNull(command, "Command must not be null!");

        Exam createdExam = _createExam(Optional.empty(), command);
        log.info("Created exam {}", createdExam);
        return createdExam;
    }

    @Transactional(readOnly = false)
    public Exam replaceExam(String token, MutateExamCommand command){
        log.debug("Trying to replace exam with token {} with command {}", token, command);
        Objects.requireNonNull(command,"Command must not be null!");

        if(!examRepository.existsExamByToken(token)) {
            log.warn("Exam with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("Exam with token %s can not be found!", token));
        }

        examRepository.deleteExamByToken(token);
        log.trace("Deleted exam with token {}", token);

        Exam replacedExam = _createExam(Optional.of(token), command);
        log.info("Successfully replaced exam {}", replacedExam);
        return replacedExam;
    }

    @Transactional(readOnly = false)
    public Exam partiallyUpdateExam(String token, MutateExamCommand command){
        log.debug("Trying to update exam with token {} with command {}", token, command);
        Objects.requireNonNull(command,"Command must not be null!");

        Optional<Exam> entity = examRepository.getExamByToken(token);

        if (entity.isEmpty()){
            log.warn("Exam with token {} can not be found!", token);
            throw new IllegalArgumentException(String.format("Exam with token %s can not be found!", token));
        }

        Exam exam = entity.get();
        if (command.date() != null) exam.setDate(command.date());
        if (command.examResult() != null) exam.setExamResult(command.examResult());
        if (command.newGradeValue() != null) exam.setNewGradeValue(command.newGradeValue());
        if (command.grade() != null) exam.setGrade(gradeRepository.findByToken(command.grade()).orElseThrow(() -> new IllegalArgumentException(String.format("Grade with token %s can not be found!", command.grade()))));

        log.info("Successfully updated exam {}", exam);
        return examRepository.save(exam);
    }

    @Transactional(readOnly = false)
    public void deleteExams(){
        log.info("Successfully deleted all exams");
        examRepository.deleteAll();
    }

    @Transactional(readOnly = false)
    public void deleteExam(String token){
        log.info("Successfully deleted exam with token {}", token);
        examRepository.deleteExamByToken(token);
    }

    private Exam _createExam(Optional<String> token, MutateExamCommand command){
        String tokenValue = token.orElseGet(tokenService::createNanoId);
        log.trace("Token value for new exam {}", tokenValue);

        Exam exam = Exam.builder()
                .examResult(command.examResult())
                .date(command.date())
                .newGradeValue(command.newGradeValue())
                .grade(gradeRepository.findByToken(command.grade()).orElse(null))
                .token(tokenValue)
                .build();

        log.trace("Mapped command {} to exam object {}", command, exam);
        return examRepository.save(exam);
    }
}
