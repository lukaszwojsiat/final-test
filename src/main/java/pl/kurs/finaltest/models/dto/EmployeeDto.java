package pl.kurs.finaltest.models.dto;

import java.time.LocalDate;

public class EmployeeDto extends PersonDto {
    private LocalDate employmentStartDate;
    private String position;
    private double salary;

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }
}
