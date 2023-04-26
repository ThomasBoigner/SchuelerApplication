package at.spengergasse.schuelerbackend.service.dto;

import at.spengergasse.schuelerbackend.domain.Subject;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public record SubjectDto(String shortname, String longname, String token, LocalDateTime creationTS, LocalDateTime updateTS) {
    public SubjectDto(Subject subject){
        this(subject.getShortname(), subject.getLongname(), subject.getToken(), subject.getCreationTS(), subject.getUpdateTS());
        log.debug("Created SubjectDto {}", this);
    }
}
