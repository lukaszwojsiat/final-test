package pl.kurs.finaltest.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class EmployeePosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    private String positionName;
    private double salary;
    private LocalDate employmentStartDate;
    private LocalDate employmentEndDate;

    public EmployeePosition() {
    }

    public EmployeePosition(Employee employee, String positionName, double salary, LocalDate employmentStartDate, LocalDate employmentEndDate) {
        this.employee = employee;
        this.positionName = positionName;
        this.salary = salary;
        this.employmentStartDate = employmentStartDate;
        this.employmentEndDate = employmentEndDate;
        this.employee.getPositions().add(this);
    }

    public Long getId() {
        return id;
    }

    public String getPositionName() {
        return positionName;
    }

    public double getSalary() {
        return salary;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public LocalDate getEmploymentEndDate() {
        return employmentEndDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePosition that = (EmployeePosition) o;
        return Double.compare(that.salary, salary) == 0 && Objects.equals(id, that.id) && Objects.equals(employee, that.employee) && Objects.equals(positionName, that.positionName) && Objects.equals(employmentStartDate, that.employmentStartDate) && Objects.equals(employmentEndDate, that.employmentEndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employee, positionName, salary, employmentStartDate, employmentEndDate);
    }
}