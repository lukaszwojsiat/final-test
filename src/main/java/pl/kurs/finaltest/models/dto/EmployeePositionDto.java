package pl.kurs.finaltest.models.dto;

import lombok.*;
import pl.kurs.finaltest.models.Employee;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EmployeePositionDto {
    private Long id;
    private Long employeeId;
    private String positionName;
    private double salary;
    private LocalDate employmentStartDate;
    private LocalDate employmentEndDate;
}
