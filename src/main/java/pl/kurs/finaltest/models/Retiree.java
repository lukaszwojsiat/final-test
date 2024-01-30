package pl.kurs.finaltest.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.models.dto.RetireeDto;

import java.io.Serial;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Retiree extends Person {
    private static final long serialVersionUID = 1L;
    private double pension;
    private int workedYears;

    public Retiree() {
    }

    public Retiree(String firstName, String lastName, String pesel, double height, double weight, String email, double pension, int workedYears) {
        super(firstName, lastName, pesel, height, weight, email);
        this.pension = pension;
        this.workedYears = workedYears;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Retiree retiree = (Retiree) o;
        return Double.compare(retiree.pension, pension) == 0 && workedYears == retiree.workedYears;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pension, workedYears);
    }

    @Override
    public Class<? extends PersonDto> dtoClassMapTo() {
        return RetireeDto.class;
    }
}
