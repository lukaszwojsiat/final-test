package pl.kurs.finaltest.models;

import jakarta.persistence.*;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.models.dto.StudentDto;

@Entity
public class Student extends Person {
    private static final long serialVersionUID = 1L;

    @Column
    private String completedUniversity;

    @Column
    private int studyYear;

    @Column
    private String fieldOfStudy;

    @Column
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
    public Class<? extends PersonDto> dtoClassMapTo() {
        return StudentDto.class;
    }
}
