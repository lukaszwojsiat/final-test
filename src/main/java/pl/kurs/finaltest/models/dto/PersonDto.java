package pl.kurs.finaltest.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class PersonDto extends RepresentationModel<PersonDto> {
    private long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private double height;
    private double weight;
    private String email;
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonDto personDto = (PersonDto) o;
        return id == personDto.id && Double.compare(personDto.height, height) == 0 && Double.compare(personDto.weight, weight) == 0 && version == personDto.version && Objects.equals(firstName, personDto.firstName) && Objects.equals(lastName, personDto.lastName) && Objects.equals(pesel, personDto.pesel) && Objects.equals(email, personDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, firstName, lastName, pesel, height, weight, email, version);
    }
}
