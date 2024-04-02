package pl.kurs.finaltest.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.models.dto.RetireeDto;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class Retiree extends Person {
    private static final long serialVersionUID = 1L;
    private double pension;
    private int workedYears;

    public Retiree(String firstName, String lastName, String pesel, double height, double weight, String email, double pension, int workedYears) {
        super(firstName, lastName, pesel, height, weight, email);
        this.pension = pension;
        this.workedYears = workedYears;
    }

    public Retiree(long id, String firstName, String lastName, String pesel, double height, double weight, String email, int version, double pension, int workedYears) {
        super(id, firstName, lastName, pesel, height, weight, email, version);
        this.pension = pension;
        this.workedYears = workedYears;
    }

    @Override
    public Class<? extends PersonDto> dtoClassMapTo() {
        return RetireeDto.class;
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
}
