package pl.kurs.finaltest.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.finaltest.models.dto.EmployeeDto;
import pl.kurs.finaltest.models.dto.PersonDto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
public class Employee extends Person {
    private static final long serialVersionUID = 1L;
    private LocalDate employmentStartDate;
    private String actualPosition;
    private double salary;
    @OneToMany(mappedBy = "employee")
    private Set<EmployeePosition> positions = new HashSet<>();

    public Employee() {
    }

    public Employee(String firstName, String lastName, String pesel, double height, double weight, String email, LocalDate employmentStartDate, String actualPosition, double salary) {
        super(firstName, lastName, pesel, height, weight, email);
        this.employmentStartDate = employmentStartDate;
        this.actualPosition = actualPosition;
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return Double.compare(employee.salary, salary) == 0 && Objects.equals(employmentStartDate, employee.employmentStartDate) && Objects.equals(actualPosition, employee.actualPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employmentStartDate, actualPosition, salary);
    }

    @Override
    public Class<? extends PersonDto> dtoClassMapTo() {
        return EmployeeDto.class;
    }
}
