package pl.kurs.finaltest.models.commands;


import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Student;

@PersonCommandSubType("CreateStudentCommand")
public class CreateStudentCommand extends CreatePersonCommand {
    private String completedUniversity;
    private Integer studyYear;
    private String fieldOfStudy;
    private double scholarship;

    public String getCompletedUniversity() {
        return completedUniversity;
    }

    public Integer getStudyYear() {
        return studyYear;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public double getScholarship() {
        return scholarship;
    }

    @Override
    public Class<? extends Student> classMapTo() {
        return Student.class;
    }
}
