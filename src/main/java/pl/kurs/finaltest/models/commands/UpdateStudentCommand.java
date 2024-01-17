package pl.kurs.finaltest.models.commands;

import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Student;

@PersonCommandSubType("UpdateStudentCommand")
public class UpdateStudentCommand extends UpdatePersonCommand{
    private String completedUniversity;
    private Integer studyYear;
    private String fieldOfStudy;
    private double scholarship;

    public String getCompletedUniversity() {
        return completedUniversity;
    }

    public void setCompletedUniversity(String completedUniversity) {
        this.completedUniversity = completedUniversity;
    }

    public Integer getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(Integer studyYear) {
        this.studyYear = studyYear;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public double getScholarship() {
        return scholarship;
    }

    public void setScholarship(double scholarship) {
        this.scholarship = scholarship;
    }

    @Override
    public Class<? extends Person> classMapTo() {
        return Student.class;
    }
}
