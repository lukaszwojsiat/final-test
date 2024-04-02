package pl.kurs.finaltest.factories.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.models.Person;

import java.time.LocalDate;
import java.util.Locale;

@Service
public class EmployeeSpecification implements PersonSpec {

    private Specification<Person> withEmploymentStartDateFrom(LocalDate employmentStartDateFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("withEmploymentStartDate"), employmentStartDateFrom);
    }

    private Specification<Person> withEmploymentStartDateTo(LocalDate employmentStartDateTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("withEmploymentStartDate"), employmentStartDateTo);
    }

    private Specification<Person> withActualPosition(String actualPosition) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("actualPosition")), "%" + actualPosition.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> withActualSalaryFrom(Double actualSalaryFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("actualSalary"), actualSalaryFrom);
    }

    private Specification<Person> withActualSalaryTo(Double actualSalaryTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("actualSalary"), actualSalaryTo);
    }

    @Override
    public Specification<Person> createSpecification(String field, String value) {
        return switch (field) {
            case "employmentStartDateFrom" -> withEmploymentStartDateFrom(LocalDate.parse(value));
            case "employmentStartDateTo" -> withEmploymentStartDateTo(LocalDate.parse(value));
            case "actualPosition" -> withActualPosition(value);
            case "actualSalaryFrom" -> withActualSalaryFrom(Double.parseDouble(value));
            case "actualSalaryTo" -> withActualSalaryTo(Double.parseDouble(value));
            default -> null;
        };
    }

}
