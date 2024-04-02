package pl.kurs.finaltest.factories.entitycreators;

import lombok.Setter;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Student;

import java.util.List;
import java.util.Map;

@Setter
@Service
public class StudentCreator implements PersonCreator {

    public StudentCreator() {
    }

    @Override
    public String getName() {
        return "Student";
    }

    @Override
    public Person createPersonToSave(Map<String, Object> params) {
        return new Student(getStringParameter("firstName", params),
                getStringParameter("lastName", params),
                getStringParameter("pesel", params),
                getDoubleParameter("height", params),
                getDoubleParameter("weight", params),
                getStringParameter("email", params),
                getStringParameter("completedUniversity", params),
                getIntegerParameter("studyYear", params),
                getStringParameter("fieldOfStudy", params),
                getDoubleParameter("scholarship", params)
        );
    }

    @Override
    public Person createPersonToUpdate(Map<String, Object> params) {
        return new Student(getLongParameter("id", params),
                getStringParameter("firstName", params),
                getStringParameter("lastName", params),
                getStringParameter("pesel", params),
                getDoubleParameter("height", params),
                getDoubleParameter("weight", params),
                getStringParameter("email", params),
                getIntegerParameter("version", params),
                getStringParameter("completedUniversity", params),
                getIntegerParameter("studyYear", params),
                getStringParameter("fieldOfStudy", params),
                getDoubleParameter("scholarship", params)
        );
    }

    @Override
    public Person createPersonFromCsv(List<String> params) {
        return new Student(params.get(0),
                params.get(1),
                params.get(2),
                Double.parseDouble(params.get(3)),
                Double.parseDouble(params.get(4)),
                params.get(5),
                params.get(6),
                Integer.parseInt(params.get(7)),
                params.get(8),
                Double.parseDouble(params.get(9))
        );
    }
}
