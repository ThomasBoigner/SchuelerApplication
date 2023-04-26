package at.spengergasse.schuelerbackend.service.dto;

import at.spengergasse.schuelerbackend.domain.Class;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public record ClassDto(String name, String token, LocalDateTime creationTS, LocalDateTime updateTS) {

    public ClassDto(Class _class) {
        this(_class.getName(), _class.getToken(), _class.getCreationTS(), _class.getUpdateTS());
        log.debug("Created ClassDto {}", this);
    }
}
