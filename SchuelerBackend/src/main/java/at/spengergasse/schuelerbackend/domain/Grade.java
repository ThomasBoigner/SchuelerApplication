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
public class Grade extends AbstractPersistable<Long> {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Min(value = 0, message = "New exam result value must be between 0 and 5!")
    @Max(value = 5, message = "New exam result value must be between 0 and 5!")
    private int gradeValue;

    @NotBlank(message = "Token must not be blank!")
    @NotNull(message = "Token must not be null!")
    private String token;
    @CreationTimestamp
    private LocalDateTime creationTS;
    @UpdateTimestamp
    private LocalDateTime updateTS;

    public Grade clearStudent(){
        this.student = null;
        return this;
    }

    public boolean hasStudent(){
        return this.student != null;
    }
}
