package at.spengergasse.schuelerbackend.service.dto;


import at.spengergasse.schuelerbackend.domain.Lesson;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public record LessonDto(String subjectShortName, String subjectLongName, ClassDto _class, TeacherDto teacher, String token, LocalDateTime creationTS, LocalDateTime updateTS) {
    public LessonDto(Lesson lesson){
        this(lesson.getSubject().getShortName(), lesson.getSubject().getLongName(), new ClassDto(lesson.get_class()), new TeacherDto(lesson.getTeacher()), lesson.getToken(), lesson.getCreationTS(), lesson.getUpdateTS());
        log.debug("Created LessonDto {}", this);
    }
}
