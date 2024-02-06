package pl.kurs.finaltest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import pl.kurs.finaltest.validations.Pesel;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UC_PERSON_EMAIL", columnNames = "email"),
        @UniqueConstraint(name = "UC_PERSON_PESEL", columnNames = "pesel")
})
@ToString
public abstract class Person implements Serializable, DtoMapClassIdentifier {
    private static final long serialVersionUID = 1L;
    @Column(insertable = false, updatable = false)
    private String type;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
//    @SequenceGenerator(name = "seqGen", sequenceName = "person_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    @Pesel
    private String pesel;
    private double height;
    private double weight;
    @Email
    private String email;
    @Version
    private int version;

    public Person() {
    }

    public Person(String type, String firstName, String lastName, @Pesel String pesel, double height, double weight, @Email String email) {
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.height = height;
        this.weight = weight;
        this.email = email;
    }

    public Person(String firstName, String lastName, String pesel, double height, double weight, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.height = height;
        this.weight = weight;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Double.compare(person.height, height) == 0 && Double.compare(person.weight, weight) == 0 && version == person.version && Objects.equals(type, person.type) && Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(pesel, person.pesel) && Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, firstName, lastName, pesel, height, weight, email, version);
    }
}
