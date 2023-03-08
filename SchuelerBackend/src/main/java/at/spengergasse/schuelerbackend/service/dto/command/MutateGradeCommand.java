package at.spengergasse.schuelerbackend.service.dto.command;

import lombok.Builder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder
public record MutateGradeCommand(String student, String lesson, @Min(1) @Max(5) int gradeValue) { }
