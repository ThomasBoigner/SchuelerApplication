package at.spengergasse.schuelerbackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Student extends AbstractPersistable<Long> {
    private String firstname;
    private String lastname;
    private String email;

    private String token;
    private LocalDateTime creationTS;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "class_id")
    private Class _class;

    private boolean conferenceDecision;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "student")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Grade> grades = new ArrayList<Grade>(5);

    public Student addGrades(Grade... grades){
        Objects.requireNonNull(grades, "Grades must not be null");

        Arrays.stream(grades).forEach(this::addGrade);
        return this;
    }

    public Student addGrade(Grade grade){
        Objects.requireNonNull(grade, "Grade must not be null!");

        if (grade.hasStudent()){
            grade.getStudent().removeGrade(grade);
        }

        grades.add(grade);
        grade.setStudent(this);
        return this;
    }

    private Student removeGrades(Grade... grades){
        Objects.requireNonNull(grades, "Grades must not be null");

        Arrays.stream(grades).forEach(this::removeGrades);
        return this;
    }

    private Student removeGrade(Grade grade) {
        Objects.requireNonNull(grade, "Grade must not be null!");

        grades.remove(grade);
        grade.clearStudent();
        return this;
    }

    public void setGrades(List<Grade> grades){
        Objects.requireNonNull(grades, "Grades must not be null!");
        if (this.grades == null){
            this.grades = new ArrayList<>(grades.size());
        }
        removeGrades(this.grades.toArray(new Grade[this.grades.size()]));
        addGrades(grades.toArray(new Grade[grades.size()]));
    }

    public List<Grade> getGrades(){
        return Collections.unmodifiableList(grades);
    }
}
