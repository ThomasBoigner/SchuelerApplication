package at.spengergasse.schuelerbackend.service;

import at.spengergasse.schuelerbackend.domain.Exam;
import at.spengergasse.schuelerbackend.foundation.TemporalValueFactory;
import at.spengergasse.schuelerbackend.persistence.ExamRepository;
import at.spengergasse.schuelerbackend.persistence.GradeRepository;
import at.spengergasse.schuelerbackend.service.dto.command.MutateExamCommand;
import at.spengergasse.schuelerbackend.service.dto.command.MutateStudentCommand;
import at.spengergasse.schuelerbackend.service.dto.command.UpdateExamCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Service
@Transactional(readOnly = true)
public class ExamService {
    private final ExamRepository examRepository;
    private final GradeRepository gradeRepository;


    private final TemporalValueFactory temporalValueFactory;
    private final TokenService tokenService;

    public List<Exam> getExams(){
        return examRepository.findAll();
    }

    public Optional<Exam> getExam(String token){
        return examRepository.getExamByToken(token);
    }

    @Transactional(readOnly = false)
    public Exam createExam(MutateExamCommand command) {
        Objects.requireNonNull(command, "Command must not be null!");

        return _createExam(Optional.empty(), command);
    }

    @Transactional(readOnly = false)
    public Exam replaceExam(String token, MutateExamCommand command){
        Objects.requireNonNull(command,"Command must not be null!");

        if(!examRepository.existsExamByToken(token)) {
            throw new IllegalArgumentException(String.format("Exam with token %s can not be found!", token));
        }

        examRepository.deleteExamByToken(token);
        return _createExam(Optional.of(token), command);
    }

    @Transactional(readOnly = false)
    public Exam partiallyUpdateExam(String token, UpdateExamCommand command){
        Objects.requireNonNull(command,"Command must not be null!");

        Optional<Exam> entity = examRepository.getExamByToken(token);
        if (!entity.isPresent()){
            throw new IllegalArgumentException(String.format("Exam with token %s can not be found!", token));
        }

        Exam exam = entity.get();
        if (command.date() != null) exam.setDate(command.date());
        if (command.examResult() != null) exam.setExamResult(command.examResult().intValue());
        if (command.newGradeValue() != null) exam.setNewGradeValue(command.newGradeValue());
        if (command.grade() != null) exam.setGrade(gradeRepository.findByToken(command.grade()).orElseThrow(() -> new IllegalArgumentException(String.format("Grade with token %s can not be found!", command.grade()))));

        return examRepository.save(exam);
    }

    @Transactional(readOnly = false)
    public void deleteExams(){
        examRepository.deleteAll();
    }

    @Transactional(readOnly = false)
    public void deleteExam(String token){
        examRepository.deleteExamByToken(token);
    }

    private Exam _createExam(Optional<String> token, MutateExamCommand command){
        LocalDateTime creationTS = temporalValueFactory.now();
        String tokenValue = token.orElseGet(() -> tokenService.createNanoId());

        Exam exam = Exam.builder()
                .examResult(command.examResult())
                .date(command.date())
                .newGradeValue(command.newGradeValue())
                .grade(gradeRepository.findByToken(command.grade()).orElse(null))
                .build();

        return examRepository.save(exam);
    }
}
