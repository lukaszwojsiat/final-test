package pl.kurs.finaltest.factories.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.models.Person;

import java.util.Locale;

@Service
public class PersonSpecification implements PersonSpec {

    private Specification<Person> withType(String type) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("type")), "%" + type.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> withFirstName(String firstName) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("firstName")), "%" + firstName.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> withLastName(String lastName) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("lastName")), "%" +lastName.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> withPesel(String pesel) {
        return (root, query, builder) -> builder.like(root.get("pesel"), "%" + pesel + "%");
    }

    private Specification<Person> withHeightFrom(Double heightFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("height"), heightFrom);
    }

    private Specification<Person> withHeightTo(Double heightTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("height"), heightTo);
    }

    private Specification<Person> withWeightFrom(Double WeightFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("weight"), WeightFrom);
    }

    private Specification<Person> withWeightTo(Double WeightTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("weight"), WeightTo);
    }

    private Specification<Person> withEmail(String email) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("email")), "%" + email.toLowerCase(Locale.ROOT) + "%");
    }

    @Override
    public Specification<Person> createSpecification(String field, String value) {
        return switch (field) {
            case "type" -> withType(value);
            case "firstName" -> withFirstName(value);
            case "lastName" -> withLastName(value);
            case "pesel" -> withPesel(value);
            case "heightFrom" -> withHeightFrom(Double.parseDouble(value));
            case "heightTo" -> withHeightTo(Double.parseDouble(value));
            case "weightFrom" -> withWeightFrom(Double.parseDouble(value));
            case "weightTo" -> withWeightTo(Double.parseDouble(value));
            case "email" -> withEmail(value);
            default -> null;
        };
    }

}
