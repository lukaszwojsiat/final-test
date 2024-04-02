package pl.kurs.finaltest.factories.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.models.Person;

@Service
public class RetireeSpecification implements PersonSpec {

    private Specification<Person> withPensionFrom(Double pensionFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("pension"), pensionFrom);
    }

    private Specification<Person> withPensionTo(Double pensionTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("pension"), pensionTo);
    }

    private Specification<Person> withWorkedYearsFrom(Integer workedYearsFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("workedYears"), workedYearsFrom);
    }

    private Specification<Person> withWorkedYearsTo(Integer workedYearsTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("workedYears"), workedYearsTo);
    }

    @Override
    public Specification<Person> createSpecification(String field, String value) {
        return switch (field) {
            case "pensionFrom" -> withPensionFrom(Double.parseDouble(value));
            case "pensionTo" -> withPensionTo(Double.parseDouble(value));
            case "workedYearsFrom" -> withWorkedYearsFrom(Integer.parseInt(value));
            case "workedYearsTo" -> withWorkedYearsTo(Integer.parseInt(value));
            default -> null;
        };
    }
}
