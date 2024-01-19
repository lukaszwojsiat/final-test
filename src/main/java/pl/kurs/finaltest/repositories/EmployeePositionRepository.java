package pl.kurs.finaltest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.finaltest.models.EmployeePosition;

import java.time.LocalDate;
import java.util.List;

public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {
    List<EmployeePosition> findAllByEmploymentStartDateBeforeAndEmploymentEndDateAfter(LocalDate endDate, LocalDate startDate);
}
