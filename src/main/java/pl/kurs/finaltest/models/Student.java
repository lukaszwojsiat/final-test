package pl.kurs.finaltest.models;

import jakarta.persistence.Entity;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.models.dto.StudentDto;

import java.util.Objects;

@Entity
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

    public String getCompletedUniversity() {
        return completedUniversity;
    }

    public int getStudyYear() {
        return studyYear;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public double getScholarship() {
        return scholarship;
    }

    public void setCompletedUniversity(String completedUniversity) {
        this.completedUniversity = completedUniversity;
    }

    public void setStudyYear(int studyYear) {
        this.studyYear = studyYear;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public void setScholarship(double scholarship) {
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
