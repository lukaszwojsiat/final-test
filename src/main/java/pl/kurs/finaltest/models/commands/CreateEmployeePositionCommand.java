package pl.kurs.finaltest.models.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateEmployeePositionCommand {
    private Long employeeId;
    private String positionName;
    private double salary;
    private LocalDate employmentStartDate;
    private LocalDate employmentEndDate;
}
