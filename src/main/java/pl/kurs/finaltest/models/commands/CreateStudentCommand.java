package pl.kurs.finaltest.models.commands;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Student;

@PersonCommandSubType("Student")
public class CreateStudentCommand extends CreatePersonCommand {
    private String completedUniversity;
    @NotNull
    private Integer studyYear;
    @NotBlank
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

    public double getScholarship() {
        return scholarship;
    }

    @Override
    public Class<? extends Student> classMapTo() {
        return Student.class;
    }
}
