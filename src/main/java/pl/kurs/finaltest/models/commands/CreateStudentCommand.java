package pl.kurs.finaltest.models.commands;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Student;

@Getter
@Setter
@PersonCommandSubType("Student")
public class CreateStudentCommand extends CreatePersonCommand {
    private String completedUniversity;
    @NotNull
    private Integer studyYear;
    @NotBlank
    private String fieldOfStudy;
    private Double scholarship;

    @Override
    public Class<? extends Student> classMapTo() {
        return Student.class;
    }
}
