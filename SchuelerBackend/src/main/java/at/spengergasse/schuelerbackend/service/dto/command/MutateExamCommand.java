package at.spengergasse.schuelerbackend.service.dto.command;

import at.spengergasse.schuelerbackend.service.dto.GradeDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MutateExamCommand(LocalDateTime date, Integer examResult, Integer newGradeValue, String grade) {
}
