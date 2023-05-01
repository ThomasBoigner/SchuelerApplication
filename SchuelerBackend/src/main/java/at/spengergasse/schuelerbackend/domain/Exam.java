package at.spengergasse.schuelerbackend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class Exam extends AbstractPersistable<Long> {
    @NotNull(message = "date must not be null!")
    private LocalDateTime date;

    @Min(value = 0, message = "Exam result value must be between 0 and 5!")
    @Max(value = 5, message = "Exam result value must be between 0 and 5!")
    private int examResult;

    @Min(value = 0, message = "New exam result value must be between 0 and 5!")
    @Max(value = 5, message = "New exam result value must be between 0 and 5!")
    private int newGradeValue;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @NotBlank(message = "Token must not be blank!")
    @NotNull(message = "Token must not be null!")
    private String token;
    @CreationTimestamp
    private LocalDateTime creationTS;
    @UpdateTimestamp
    private LocalDateTime updateTS;
}
