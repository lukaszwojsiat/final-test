package pl.kurs.finaltest.models.commands;

import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Employee;

import java.time.LocalDate;


@PersonCommandSubType("CreateEmployeeDto")
public class CreateEmployeeCommand extends CreatePersonCommand {
    private LocalDate employmentStartDate;
    private String position;
    private double salary;

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
