package at.spengergasse.schuelerbackend.service.dto;

import at.spengergasse.schuelerbackend.domain.Teacher;

import java.time.LocalDateTime;

public record TeacherDto(String firstname, String lastname, String email, String token, LocalDateTime creationTS) {
    public TeacherDto(Teacher teacher){
        this(teacher.getFirstname(), teacher.getLastname(), teacher.getEmail(), teacher.getToken(), teacher.getCreationTS());
    }
}
