package pl.kurs.finaltest.models.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public abstract class PersonDto extends RepresentationModel<PersonDto> {
    private long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private double height;
    private double weight;
    private String email;
    private int version;

    public PersonDto() {
    }

    public PersonDto(long id, String firstName, String lastName, String pesel, double height, double weight, String email, int version) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.height = height;
        this.weight = weight;
        this.email = email;
        this.version = version;
    }
}
