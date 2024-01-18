package pl.kurs.finaltest.models.commands;

import jakarta.validation.constraints.NotNull;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Retiree;


@PersonCommandSubType("Retiree")
public class UpdateRetireeCommand extends UpdatePersonCommand {
    @NotNull
    private Double pension;
    @NotNull
    private Integer workedYears;

    public double getPension() {
        return pension;
    }

    public int getWorkedYears() {
        return workedYears;
    }

    @Override
    public Class<? extends Person> classMapTo() {
        return Retiree.class;
    }
}
