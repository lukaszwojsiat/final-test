package pl.kurs.finaltest.models.commands;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Student;

@Getter
@Setter
@PersonCommandSubType("Student")
public class UpdateStudentCommand extends UpdatePersonCommand {
    private String completedUniversity;
    @NotNull
    private Integer studyYear;
    @NotNull
    private String fieldOfStudy;
    private Double scholarship;

    @Override
    public Class<? extends Person> classMapTo() {
        return Student.class;
    }
}
