package at.spengergasse.schuelerbackend.service.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
public record MutateTeacherCommand(@NotBlank String firstname, @NotBlank String lastname, @NotBlank String email) { }
