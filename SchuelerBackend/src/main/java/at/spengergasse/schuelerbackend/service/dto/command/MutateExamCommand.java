package at.spengergasse.schuelerbackend.service.dto.command;

import at.spengergasse.schuelerbackend.service.dto.GradeDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MutateExamCommand(@NotNull LocalDateTime date, @Min(0) @Max(5) int examResult, @Min(0) @Max(5) int newGradeValue, @NotNull String grade) {
}
