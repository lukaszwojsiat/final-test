package pl.kurs.finaltest.specifications;

import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;
import pl.kurs.finaltest.models.Person;


@And({
        @Spec(path = "type", spec = LikeIgnoreCase.class),
        @Spec(path = "firstName", spec = LikeIgnoreCase.class),
        @Spec(path = "lastName", spec = LikeIgnoreCase.class),
        @Spec(path = "pesel", spec = LikeIgnoreCase.class),
        @Spec(path = "email", spec = LikeIgnoreCase.class),
        @Spec(path = "employmentStartDate", params = {"employmentStartDateFrom", "employmentStartDateTo"}, spec = Between.class),
        @Spec(path = "actualPosition", spec = LikeIgnoreCase.class),
        @Spec(path = "actualSalary", params = {"salaryFrom", "salaryTo"}, spec = Between.class),
        @Spec(path = "completedUniversity", spec = LikeIgnoreCase.class),
        @Spec(path = "studyYear", params = {"studyYearFrom", "studyYearTo"}, spec = Between.class),
        @Spec(path = "fieldOfStudy", spec = LikeIgnoreCase.class),
        @Spec(path = "scholarship", params = {"scholarshipFrom", "scholarshipTo"}, spec = Between.class),
        @Spec(path = "pension", params = {"pensionFrom", "pensionTo"}, spec = Between.class),
        @Spec(path = "workedYears", params = {"workedYearsFrom", "workedYearsTo"}, spec = Between.class)
})
public interface PersonSpecification extends Specification<Person> {
}
