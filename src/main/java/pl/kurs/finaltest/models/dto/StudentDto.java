package pl.kurs.finaltest.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class StudentDto extends PersonDto {
    private String completedUniversity;
    private Integer studyYear;
    private String fieldOfStudy;
    private double scholarship;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StudentDto that = (StudentDto) o;
        return Double.compare(that.scholarship, scholarship) == 0 && Objects.equals(completedUniversity, that.completedUniversity) && Objects.equals(studyYear, that.studyYear) && Objects.equals(fieldOfStudy, that.fieldOfStudy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), completedUniversity, studyYear, fieldOfStudy, scholarship);
    }
}
