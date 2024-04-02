package pl.kurs.finaltest.factories.specifications;

import org.springframework.data.jpa.domain.Specification;
import pl.kurs.finaltest.models.Person;

public interface PersonSpec {

    Specification<Person> createSpecification(String field, String value);
}
