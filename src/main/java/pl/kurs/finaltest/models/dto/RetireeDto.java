package pl.kurs.finaltest.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetireeDto extends PersonDto {
    private double pension;
    private Integer workedYears;

}
