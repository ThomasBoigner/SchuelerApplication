package at.spengergasse.schuelerbackend.service.dto;

import at.spengergasse.schuelerbackend.domain.Student;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public record StudentDto(String firstname, String lastname, String email, ClassDto _class, boolean conferenceDecision, List<GradeDto> gradeValues, String token, LocalDateTime creationTS, LocalDateTime updateTS) {
    public StudentDto(Student student){
        this(student.getFirstname(), student.getLastname(), student.getEmail(), new ClassDto(student.get_class()), student.isConferenceDecision(), student.getGrades().stream().map(GradeDto::new).toList(), student.getToken(), student.getCreationTS(), student.getUpdateTS());
        log.debug("Created StudentDto {}", this);
    }
}
