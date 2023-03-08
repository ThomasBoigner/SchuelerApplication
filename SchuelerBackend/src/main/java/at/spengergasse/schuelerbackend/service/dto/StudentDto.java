package at.spengergasse.schuelerbackend.service.dto;

import at.spengergasse.schuelerbackend.domain.Student;

import java.time.LocalDateTime;
import java.util.List;

public record StudentDto(String firstname, String lastname, String email, ClassDto _class, boolean conferenceDecision, List<GradeDto> gradeValues, String token, LocalDateTime creationTS) {
    public StudentDto(Student student){
        this(student.getFirstname(), student.getLastname(), student.getEmail(), new ClassDto(student.get_class()), student.isConferenceDecision(), student.getGrades().stream().map(GradeDto::new).toList(), student.getToken(), student.getCreationTS());
    }
}
