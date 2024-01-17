package pl.kurs.finaltest.models.commands;


import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
public abstract class CreatePersonCommand implements CommandMapClassIdentifier {
    private String type;
    private String firstName;
    private String lastName;
    private String pesel;
    private double height;
    private double weight;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public String getEmail() {
        return email;
    }
}
