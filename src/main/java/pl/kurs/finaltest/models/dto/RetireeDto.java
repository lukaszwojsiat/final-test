package pl.kurs.finaltest.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class RetireeDto extends PersonDto {
    private double pension;
    private Integer workedYears;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RetireeDto that = (RetireeDto) o;
        return Double.compare(that.pension, pension) == 0 && Objects.equals(workedYears, that.workedYears);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pension, workedYears);
    }
}
