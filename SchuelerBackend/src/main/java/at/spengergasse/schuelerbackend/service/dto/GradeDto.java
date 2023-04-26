package at.spengergasse.schuelerbackend.service.dto;


import at.spengergasse.schuelerbackend.domain.Grade;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public record GradeDto(LessonDto lesson, int gradeValue, String token, LocalDateTime creationTS, LocalDateTime updateTS) {
    public GradeDto(Grade grade){
        this(new LessonDto(grade.getLesson()), grade.getGradeValue(), grade.getToken(), grade.getCreationTS(), grade.getUpdateTS());
        log.debug("Created GradeDto {}", this);
    }
}
