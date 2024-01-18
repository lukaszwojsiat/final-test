package pl.kurs.finaltest.models.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Employee;

import java.time.LocalDate;


@PersonCommandSubType("Employee")
public class CreateEmployeeCommand extends CreatePersonCommand {
    @PastOrPresent
    private LocalDate employmentStartDate;
    @NotBlank
    private String position;
    @NotNull
    private Double salary;

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public Class<? extends Employee> classMapTo() {
        return Employee.class;
    }
}
