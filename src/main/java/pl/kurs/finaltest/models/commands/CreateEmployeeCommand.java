package pl.kurs.finaltest.models.commands;

import lombok.Getter;
import lombok.Setter;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Employee;

import java.time.LocalDate;

@Getter
@Setter
@PersonCommandSubType("CreateEmployeeDto")
public class CreateEmployeeCommand extends CreatePersonCommand {
    private LocalDate employmentStartDate;
    private String actualPosition;
    private double actualSalary;

    @Override
    public Class<? extends Employee> classMapTo() {
        return Employee.class;
    }
}
