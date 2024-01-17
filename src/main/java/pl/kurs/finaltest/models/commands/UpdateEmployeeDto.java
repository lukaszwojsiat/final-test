package pl.kurs.finaltest.models.commands;

import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;

import java.time.LocalDate;

@PersonCommandSubType("UpdateStudentCommand")
public class UpdateEmployeeDto extends UpdatePersonCommand{
    private LocalDate employmentStartDate;
    private String position;
    private double salary;

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public Class<? extends Person> classMapTo() {
        return Employee.class;
    }
}
