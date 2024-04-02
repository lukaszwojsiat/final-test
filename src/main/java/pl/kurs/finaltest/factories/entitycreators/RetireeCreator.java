package pl.kurs.finaltest.factories.entitycreators;

import org.springframework.stereotype.Service;
import pl.kurs.finaltest.exceptions.WrongPersonInformationException;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Retiree;

import java.util.List;
import java.util.Map;

@Service
public class RetireeCreator implements PersonCreator {

    @Override
    public String getName() {
        return "Retiree";
    }

    @Override
    public Person createPersonToSave(Map<String, Object> params) {
        return new Retiree(getStringParameter("firstName", params),
                getStringParameter("lastName", params),
                getStringParameter("pesel", params),
                getDoubleParameter("height", params),
                getDoubleParameter("weight", params),
                getStringParameter("email", params),
                getDoubleParameter("pension", params),
                getIntegerParameter("workedYears", params)
        );
    }

    @Override
    public Person createPersonToUpdate(Map<String, Object> params) {
        return new Retiree(getLongParameter("id", params),
                getStringParameter("firstName", params),
                getStringParameter("lastName", params),
                getStringParameter("pesel", params),
                getDoubleParameter("height", params),
                getDoubleParameter("weight", params),
                getStringParameter("email", params),
                getIntegerParameter("version", params),
                getDoubleParameter("pension", params),
                getIntegerParameter("workedYears", params)
        );
    }

    @Override
    public Person createPersonFromCsv(List<String> params) {
        return new Retiree(params.get(0),
                params.get(1),
                params.get(2),
                Double.parseDouble(params.get(3)),
                Double.parseDouble(params.get(4)),
                params.get(5),
                Double.parseDouble(params.get(6)),
                Integer.parseInt(params.get(7))
        );
    }
}
