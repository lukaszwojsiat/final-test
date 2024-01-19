package pl.kurs.finaltest.models.commands;

import pl.kurs.finaltest.models.Employee;

import java.time.LocalDate;

public class CreateEmployeePositionCommand {
    private Long employeeId;
    private String positionName;
    private double salary;
    private LocalDate employmentStartDate;
    private LocalDate employmentEndDate;

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getPositionName() {
        return positionName;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public LocalDate getEmploymentEndDate() {
        return employmentEndDate;
    }
}
