package pl.kurs.finaltest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kurs.finaltest.models.EmployeePosition;

import java.util.List;

@Repository
public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {

    @Query("SELECT ep FROM EmployeePosition ep LEFT JOIN FETCH ep.employee e WHERE ep.employee.id = :id")
    List<EmployeePosition> getEmployeePositionByEmployeeId(Long id);
}
