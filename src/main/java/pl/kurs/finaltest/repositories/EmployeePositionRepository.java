package pl.kurs.finaltest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.finaltest.models.EmployeePosition;

public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {
}
