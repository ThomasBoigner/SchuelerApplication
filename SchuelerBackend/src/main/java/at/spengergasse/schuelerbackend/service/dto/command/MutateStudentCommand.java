package at.spengergasse.schuelerbackend.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

public record MutateStudentCommand (@NotBlank String firstname, @NotBlank String lastname, @NotBlank String email, String _class, boolean conferenceDecision, @NotNull List<String> grades){ }
