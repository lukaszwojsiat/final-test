package pl.kurs.finaltest.models.commands;

import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Employee;

import java.time.LocalDate;


@PersonCommandSubType("CreateEmployeeDto")
public class CreateEmployeeCommand extends CreatePersonCommand {
    private LocalDate employmentStartDate;
    private String actualPosition;
    private double actualSalary;

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public String getActualPosition() {
        return actualPosition;
    }

    public double getActualSalary() {
        return actualSalary;
    }

    @Override
    public Class<? extends Employee> classMapTo() {
        return Employee.class;
    }
}
