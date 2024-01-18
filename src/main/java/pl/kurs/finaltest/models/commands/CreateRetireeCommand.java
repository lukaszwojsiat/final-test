package pl.kurs.finaltest.models.commands;


import jakarta.validation.constraints.NotNull;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Retiree;

@PersonCommandSubType("Retiree")
public class CreateRetireeCommand extends CreatePersonCommand{
    @NotNull
    private Double pension;
    @NotNull
    private Integer workedYears;

    public double getPension() {
        return pension;
    }

    public Integer getWorkedYears() {
        return workedYears;
    }


    @Override
    public Class<? extends Retiree> classMapTo() {
        return Retiree.class;
    }
}
