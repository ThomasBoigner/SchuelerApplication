package at.spengergasse.schuelerbackend.service.dto.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public record UpdateStudentCommand (String firstname, String lastname, String email, String _class, Boolean conferenceDecision, List<String> grades){ }
