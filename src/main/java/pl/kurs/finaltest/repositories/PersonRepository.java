package pl.kurs.finaltest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    @Query(value = "select e from Employee e left join fetch e.positions where e.id = :id")
    Optional<Employee> getEmployeeWithPositions(Long id);
}
