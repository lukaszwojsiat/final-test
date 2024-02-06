package pl.kurs.finaltest.models;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import lombok.*;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.models.dto.StudentDto;
import pl.kurs.finaltest.validations.Pesel;

import java.util.Objects;

@AllArgsConstructor()
@Getter
@Setter
@Entity
@ToString
public class Student extends Person {
    private static final long serialVersionUID = 1L;
    private String completedUniversity;
    private int studyYear;
    private String fieldOfStudy;
    private double scholarship;

    public Student() {
    }

    public Student(String firstName, String lastName, String pesel, double height, double weight, String email, String completedUniversity, Integer studyYear, String fieldOfStudy, double scholarship) {
        super(firstName, lastName, pesel, height, weight, email);
        this.completedUniversity = completedUniversity;
        this.studyYear = studyYear;
        this.fieldOfStudy = fieldOfStudy;
        this.scholarship = scholarship;
    }

    public Student(String type, String firstName, String lastName, @Pesel String pesel, double height, double weight, @Email String email, String completedUniversity, int studyYear, String fieldOfStudy, double scholarship) {
        super(type, firstName, lastName, pesel, height, weight, email);
        this.completedUniversity = completedUniversity;
        this.studyYear = studyYear;
        this.fieldOfStudy = fieldOfStudy;
        this.scholarship = scholarship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return studyYear == student.studyYear && Double.compare(student.scholarship, scholarship) == 0 && Objects.equals(completedUniversity, student.completedUniversity) && Objects.equals(fieldOfStudy, student.fieldOfStudy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), completedUniversity, studyYear, fieldOfStudy, scholarship);
    }

    @Override
    public Class<? extends PersonDto> dtoClassMapTo() {
        return StudentDto.class;
    }
}
