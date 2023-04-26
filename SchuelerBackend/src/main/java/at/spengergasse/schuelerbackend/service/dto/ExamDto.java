package at.spengergasse.schuelerbackend.service.dto;

import at.spengergasse.schuelerbackend.domain.Exam;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public record ExamDto(LocalDateTime date, int examResult, int newGradeValue, GradeDto grade, String token, LocalDateTime creationTS, LocalDateTime updateTS) {
    public ExamDto(Exam exam){
       this(exam.getDate(), exam.getExamResult(), exam.getNewGradeValue(), new GradeDto(exam.getGrade()), exam.getToken(), exam.getCreationTS(), exam.getUpdateTS());
        log.debug("Created ExamDto {}", this);
    }
}
