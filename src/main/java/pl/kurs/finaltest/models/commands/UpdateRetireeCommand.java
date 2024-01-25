package pl.kurs.finaltest.models.commands;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Retiree;

@Getter
@Setter
@PersonCommandSubType("Retiree")
public class UpdateRetireeCommand extends UpdatePersonCommand {
    @NotNull
    private Double pension;
    @NotNull
    private Integer workedYears;

    @Override
    public Class<? extends Person> classMapTo() {
        return Retiree.class;
    }
}
