package pl.kurs.finaltest.factories.entitycreators;

import org.springframework.stereotype.Service;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeCreator implements PersonCreator {
    @Override
    public String getName() {
        return "Employee";
    }

    @Override
    public Person createPersonToSave(Map<String, Object> params) {
        return new Employee(getStringParameter("firstName", params),
                getStringParameter("lastName", params),
                getStringParameter("pesel", params),
                getDoubleParameter("height", params),
                getDoubleParameter("weight", params),
                getStringParameter("email", params),
                getLocalDateParameter("employmentStartDate", params),
                getStringParameter("actualPosition", params),
                getDoubleParameter("actualSalary", params)
        );
    }

    @Override
    public Person createPersonToUpdate(Map<String, Object> params) {
        return new Employee(getLongParameter("id", params),
                getStringParameter("firstName", params),
                getStringParameter("lastName", params),
                getStringParameter("pesel", params),
                getDoubleParameter("height", params),
                getDoubleParameter("weight", params),
                getStringParameter("email", params),
                getIntegerParameter("version", params),
                getLocalDateParameter("employmentStartDate", params),
                getStringParameter("actualPosition", params),
                getDoubleParameter("actualSalary", params)
        );
    }

    @Override
    public Person createPersonFromCsv(List<String> params) {
        return new Employee(params.get(0),
                params.get(1),
                params.get(2),
                Double.parseDouble(params.get(3)),
                Double.parseDouble(params.get(4)),
                params.get(5),
                LocalDate.parse(params.get(6)),
                params.get(7),
                Double.parseDouble(params.get(8))
        );
    }

}
