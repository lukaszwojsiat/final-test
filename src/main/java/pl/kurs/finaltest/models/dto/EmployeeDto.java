package pl.kurs.finaltest.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDto extends PersonDto {
    private LocalDate employmentStartDate;
    private String actualPosition;
    private double actualSalary;

    public EmployeeDto() {
    }

    public EmployeeDto(long id, String firstName, String lastName, String pesel, double height, double weight, String email, int version, LocalDate employmentStartDate, String actualPosition, double actualSalary) {
        super(id, firstName, lastName, pesel, height, weight, email, version);
        this.employmentStartDate = employmentStartDate;
        this.actualPosition = actualPosition;
        this.actualSalary = actualSalary;
    }
}
