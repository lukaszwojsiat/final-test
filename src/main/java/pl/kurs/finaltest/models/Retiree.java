package pl.kurs.finaltest.models;

import jakarta.persistence.*;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.models.dto.RetireeDto;

@Entity
public class Retiree extends Person {
    private static final long serialVersionUID = 1L;

    @Column
    private double pension;

    @Column
    private int workedYears;

    public Retiree() {
    }

    public Retiree(String firstName, String lastName, String pesel, double height, double weight, String email, double pension, int workedYears) {
        super(firstName, lastName, pesel, height, weight, email);
        this.pension = pension;
        this.workedYears = workedYears;
    }

    public double getPension() {
        return pension;
    }

    public int getWorkedYears() {
        return workedYears;
    }

    public void setPension(double pension) {
        this.pension = pension;
    }

    public void setWorkedYears(int workedYears) {
        this.workedYears = workedYears;
    }

    @Override
    public Class<? extends PersonDto> dtoClassMapTo() {
        return RetireeDto.class;
    }
}
