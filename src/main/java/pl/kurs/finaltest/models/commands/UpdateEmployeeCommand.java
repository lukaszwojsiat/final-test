package pl.kurs.finaltest.models.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;

import java.time.LocalDate;

@PersonCommandSubType("Employee")
public class UpdateEmployeeCommand extends UpdatePersonCommand {
    @PastOrPresent
    private LocalDate employmentStartDate;
    @NotBlank
    private String actualPosition;
    @NotNull
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
    public Class<? extends Person> classMapTo() {
        return Employee.class;
    }
}
