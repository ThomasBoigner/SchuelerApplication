package at.spengergasse.schuelerbackend.service.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
public record MutateLessonCommand(@NotNull String subject, @NotNull String _class, @NotNull String teacherCommand) { }
