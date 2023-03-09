package at.spengergasse.schuelerbackend.service.dto.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

    public record UpdateExamCommand(LocalDateTime date, @Min(0) @Max(5) Integer examResult, @Min(0) @Max(5) Integer newGradeValue, String grade) { }

