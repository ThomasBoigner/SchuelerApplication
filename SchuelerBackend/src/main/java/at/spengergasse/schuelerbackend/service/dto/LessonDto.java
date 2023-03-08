package at.spengergasse.schuelerbackend.service.dto;


import at.spengergasse.schuelerbackend.domain.Lesson;

import java.time.LocalDateTime;

public record LessonDto(SubjectDto subject, ClassDto _class, TeacherDto teacher, String token, LocalDateTime creationTS) {
    public LessonDto(Lesson lesson){
        this(new SubjectDto(lesson.getSubject()), new ClassDto(lesson.get_class()), new TeacherDto(lesson.getTeacher()), lesson.getToken(), lesson.getCreationTS());
    }
}
