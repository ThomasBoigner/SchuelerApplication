package at.spengergasse.schuelerbackend.service.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public record MutateLessonCommand(String subject, String _class, String teacherCommand) { }
