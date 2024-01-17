package pl.kurs.finaltest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.finaltest.models.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByFirstName(String firstName);

    List<Employee> findAllByLastName(String lastName);
}