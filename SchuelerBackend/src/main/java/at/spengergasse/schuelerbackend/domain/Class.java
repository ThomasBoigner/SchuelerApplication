package at.spengergasse.schuelerbackend.domain;

import jakarta.persistence.Entity;
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
public class Class extends AbstractPersistable<Long> {
    private String name;

    private String token;
    @CreationTimestamp
    private LocalDateTime creationTS;
    @UpdateTimestamp
    private LocalDateTime updateTS;

}
