package pl.kurs.finaltest.models.commands;

import jakarta.validation.constraints.NotNull;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Student;

@PersonCommandSubType("Student")
public class UpdateStudentCommand extends UpdatePersonCommand {
    private String completedUniversity;
    @NotNull
    private Integer studyYear;
    @NotNull
    private String fieldOfStudy;
    private Double scholarship;

    public String getCompletedUniversity() {
        return completedUniversity;
    }

    public Integer getStudyYear() {
        return studyYear;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public Double getScholarship() {
        return scholarship;
    }

    @Override
    public Class<? extends Person> classMapTo() {
        return Student.class;
    }
}
