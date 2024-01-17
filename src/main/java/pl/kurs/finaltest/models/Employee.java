package pl.kurs.finaltest.models;

import jakarta.persistence.*;
import pl.kurs.finaltest.models.dto.EmployeeDto;
import pl.kurs.finaltest.models.dto.PersonDto;

import java.time.LocalDate;

@Entity
public class Employee extends Person {
    private static final long serialVersionUID = 1L;

    @Column
    private LocalDate employmentStartDate;

    @Column
    private String position;

    @Column
    private double salary;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String pesel, double height, double weight, String email, LocalDate employmentStartDate, String position, double salary) {
        super(firstName, lastName, pesel, height, weight, email);
        this.employmentStartDate = employmentStartDate;
        this.position = position;
        this.salary = salary;
    }

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
    public Class<? extends PersonDto> dtoClassMapTo() {
        return EmployeeDto.class;
    }
}
