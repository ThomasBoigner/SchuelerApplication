package at.spengergasse.schuelerbackend.service.dto;

import at.spengergasse.schuelerbackend.domain.Exam;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExamDto(LocalDateTime date, int examResult, int newGradeValue, GradeDto grade, String token, LocalDateTime creationTS) {
    public ExamDto(Exam exam){
       this(exam.getDate(), exam.getExamResult(), exam.getNewGradeValue(), new GradeDto(exam.getGrade()), exam.getToken(), exam.getCreationTS());
    }
}
