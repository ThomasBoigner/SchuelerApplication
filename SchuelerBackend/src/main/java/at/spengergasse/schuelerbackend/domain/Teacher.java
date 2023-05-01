package at.spengergasse.schuelerbackend.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Teacher extends AbstractPersistable<Long> {
    @NotBlank(message = "Firstname must not be blank!")
    @NotNull(message = "Firstname must not be null!")
    private String firstname;
    @NotBlank(message = "Lastname must not be blank!")
    @NotNull(message = "Lastname must not be null!")
    private String lastname;
    @NotBlank(message = "E-mail must not be blank!")
    @NotNull(message = "E-mail must not be null!")
    private String email;

    @NotBlank(message = "Token must not be blank!")
    @NotNull(message = "Token must not be null!")
    private String token;
    @CreationTimestamp
    private LocalDateTime creationTS;
    @UpdateTimestamp
    private LocalDateTime updateTS;
}
