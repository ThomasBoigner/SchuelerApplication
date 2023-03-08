package at.spengergasse.schuelerbackend.service.dto.command;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
public record MutateStudentCommand (@NotBlank String firstname, @NotBlank String lastname, @NotBlank String email, String _class, boolean conferenceDecision, @NotNull List<String> grades){ }
