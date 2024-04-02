package pl.kurs.finaltest.factories.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.models.Person;

import java.util.Locale;

@Service
public class StudentSpecification implements PersonSpec {
    private Specification<Person> withCompletedUniversity(String completedUniversity) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("completedUniversity")), "%" + completedUniversity.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> withStudyYearFrom(Integer studyYearFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("studyYear"), studyYearFrom);
    }

    private Specification<Person> withStudyYearTo(Integer studyYearTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("studyYear"), studyYearTo);
    }

    private Specification<Person> withFieldOfStudy(String fieldOfStudy) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("fieldOfStudy")), "%" + fieldOfStudy.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<Person> withScholarshipFrom(Double scholarshipFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("studyYear"), scholarshipFrom);
    }

    private Specification<Person> withScholarshipTo(Double scholarshipTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("studyYear"), scholarshipTo);
    }

    @Override
    public Specification<Person> createSpecification(String field, String value) {
        return switch (field) {
            case "completedUniversity" -> withCompletedUniversity(value);
            case "studyYearFrom" -> withStudyYearFrom(Integer.parseInt(value));
            case "studyYearTo" -> withStudyYearTo(Integer.parseInt(value));
            case "fieldOfStudy" -> withFieldOfStudy(value);
            case "scholarshipFrom" -> withScholarshipFrom(Double.parseDouble(value));
            case "scholarshipTo" -> withScholarshipTo(Double.parseDouble(value));
            default -> null;
        };
    }
}
