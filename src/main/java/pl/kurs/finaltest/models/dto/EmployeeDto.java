package pl.kurs.finaltest.models.dto;

import java.time.LocalDate;

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

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public void setActualPosition(String actualPosition) {
        this.actualPosition = actualPosition;
    }

    public void setActualSalary(double actualSalary) {
        this.actualSalary = actualSalary;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public String getActualPosition() {
        return actualPosition;
    }

    public double getActualSalary() {
        return actualSalary;
    }
}
