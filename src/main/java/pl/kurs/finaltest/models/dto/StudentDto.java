package pl.kurs.finaltest.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDto extends PersonDto {
    private String completedUniversity;
    private Integer studyYear;
    private String fieldOfStudy;
    private double scholarship;
}
