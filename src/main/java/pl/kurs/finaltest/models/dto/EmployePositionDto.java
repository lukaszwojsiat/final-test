package pl.kurs.finaltest.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kurs.finaltest.models.Employee;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployePositionDto {
    private Long id;
    private Long employeeId;
    private String positionName;
    private double salary;
    private LocalDate employmentStartDate;
    private LocalDate employmentEndDate;
}
