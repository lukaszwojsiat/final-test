package pl.kurs.finaltest.models.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
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
    @NotNull
    private Long employeeId;
    @NotBlank
    private String positionName;
    @NotNull
    private double salary;
    @PastOrPresent
    private LocalDate employmentStartDate;
    private LocalDate employmentEndDate;
}
