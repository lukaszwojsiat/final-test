package pl.kurs.finaltest.models.commands;

import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Retiree;


@PersonCommandSubType("UpdateStudentCommand")
public class UpdateRetireeCommand extends UpdatePersonCommand{
    private double pension;
    private int workedYears;

    public double getPension() {
        return pension;
    }

    public void setPension(double pension) {
        this.pension = pension;
    }

    public int getWorkedYears() {
        return workedYears;
    }

    public void setWorkedYears(int workedYears) {
        this.workedYears = workedYears;
    }

    @Override
    public Class<? extends Person> classMapTo() {
        return Retiree.class;
    }
}
