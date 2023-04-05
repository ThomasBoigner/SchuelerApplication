package at.spengergasse.schuelerbackend.service.dto.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;

public record MutateGradeCommand(String student, String lesson, @Min(1) @Max(5) int gradeValue) { }
