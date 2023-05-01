package at.spengergasse.schuelerbackend.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record MutateTeacherCommand(String firstname, String lastname, String email) { }
