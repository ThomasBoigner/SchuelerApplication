package at.spengergasse.schuelerbackend.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record MutateClassCommand(@NotBlank String name) { }
