package pl.kurs.finaltest.models.commands;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.finaltest.validations.Pesel;

@Getter
@Setter
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
}
