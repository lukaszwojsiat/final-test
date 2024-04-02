package pl.kurs.finaltest.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class EmployeeDto extends PersonDto {
    private LocalDate employmentStartDate;
    private String actualPosition;
    private double actualSalary;

    public EmployeeDto(long id, String firstName, String lastName, String pesel, double height, double weight, String email, int version, LocalDate employmentStartDate, String actualPosition, double actualSalary) {
        super(id, firstName, lastName, pesel, height, weight, email, version);
        this.employmentStartDate = employmentStartDate;
        this.actualPosition = actualPosition;
        this.actualSalary = actualSalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EmployeeDto that = (EmployeeDto) o;
        return Double.compare(that.actualSalary, actualSalary) == 0 && Objects.equals(employmentStartDate, that.employmentStartDate) && Objects.equals(actualPosition, that.actualPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employmentStartDate, actualPosition, actualSalary);
    }
}
