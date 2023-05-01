package at.spengergasse.schuelerbackend.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
public class Subject {
    @NotBlank(message = "Short Name must not be blank!")
    @NotNull(message = "Short Name must not be null!")
    private String shortName;
    @NotBlank(message = "Long Name must not be blank!")
    @NotNull(message = "Long Name must not be null!")
    private String longName;
}
