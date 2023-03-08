package at.spengergasse.schuelerbackend.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Teacher extends AbstractPersistable<Long> {
    private String firstname;
    private String lastname;
    private String email;

    private String token;
    private LocalDateTime creationTS;
}
