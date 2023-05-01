package at.spengergasse.schuelerbackend.service.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record MutateLessonCommand(String shortName, String longName, String _class, String teacher) { }
