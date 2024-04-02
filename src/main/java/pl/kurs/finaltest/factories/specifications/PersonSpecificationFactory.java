package pl.kurs.finaltest.factories.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.models.Person;

import java.util.Set;

@Service
public class PersonSpecificationFactory {
    private final Set<PersonSpec> personSpecs;

    public PersonSpecificationFactory(Set<PersonSpec> specifications) {
        this.personSpecs = specifications;
    }

    public Specification<Person> getSpecification(String field, String value) {
        Specification<Person> spec = null;
        for (PersonSpec specs : personSpecs) {
            if ((spec = specs.createSpecification(field, value)) != null)
                break;
        }
        return spec;
    }
}
