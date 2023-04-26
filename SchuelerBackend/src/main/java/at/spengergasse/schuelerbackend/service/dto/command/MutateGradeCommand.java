package at.spengergasse.schuelerbackend.service.dto.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record MutateGradeCommand(@NotBlank @NotNull String student, @NotBlank @NotNull String lesson, @Min(1) @Max(5) int gradeValue) { }
