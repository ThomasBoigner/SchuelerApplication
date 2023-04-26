package at.spengergasse.schuelerbackend.service.dto;

import at.spengergasse.schuelerbackend.domain.Teacher;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public record TeacherDto(String firstname, String lastname, String email, String token, LocalDateTime creationTS, LocalDateTime updateTS) {
    public TeacherDto(Teacher teacher){
        this(teacher.getFirstname(), teacher.getLastname(), teacher.getEmail(), teacher.getToken(), teacher.getCreationTS(), teacher.getUpdateTS());
        log.debug("Created TeacherDto {}", this);
    }
}
