package pl.kurs.finaltest.models.commands;


import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Retiree;

@PersonCommandSubType("CreateRetireeCommand")
public class CreateRetireeCommand extends CreatePersonCommand{
    private double pension;
    private int workedYears;

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
