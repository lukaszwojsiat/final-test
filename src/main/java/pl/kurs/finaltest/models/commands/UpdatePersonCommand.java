package pl.kurs.finaltest.models.commands;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.kurs.finaltest.validations.Pesel;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
public abstract class UpdatePersonCommand implements CommandMapClassIdentifier {
    @NotNull
    private Long id;
    private String type;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Pesel
    private String pesel;
    private Double height;
    private Double weight;
    @Email
    private String email;
    @NotNull
    private Integer version;

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWeight() {
        return weight;
    }

    public String getEmail() {
        return email;
    }

    public Integer getVersion() {
        return version;
    }
}
