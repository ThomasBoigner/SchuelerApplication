package at.spengergasse.schuelerbackend.service.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public record MutateSubjectCommand(@NotBlank String shortname, @NotBlank String longname) { }
