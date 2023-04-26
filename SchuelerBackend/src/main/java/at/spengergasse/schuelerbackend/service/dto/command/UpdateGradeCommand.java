package at.spengergasse.schuelerbackend.service.dto.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateGradeCommand(String student, String lesson, @Min(1) @Max(5) Integer gradeValue) {
}
