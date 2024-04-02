package pl.kurs.finaltest.factories.entitycreators;

import pl.kurs.finaltest.models.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PersonCreator {
    String getName();

    Person createPersonToSave(Map<String, Object> params);

    Person createPersonToUpdate(Map<String, Object> params);

    Person createPersonFromCsv(List<String> params);

    default String getStringParameter(String name, Map<String, Object> parameters) {
        if (!parameters.containsKey(name))
            return null;
        return (String) parameters.get(name);
    }

    default Integer getIntegerParameter(String name, Map<String, Object> parameters) {
        if (!parameters.containsKey(name))
            return 0;
        return (Integer) parameters.get(name);
    }

    default Double getDoubleParameter(String name, Map<String, Object> parameters) {
        if (!parameters.containsKey(name))
            return 0.0;
        return (Double) parameters.get(name);
    }

    default LocalDate getLocalDateParameter(String name, Map<String, Object> parameters) {
        if (!parameters.containsKey(name))
            return null;
        return (LocalDate) parameters.get(name);
    }

    default Long getLongParameter(String name, Map<String, Object> parameters) {
        if (!parameters.containsKey(name))
            return 0L;
        return ((Integer) parameters.get(name)).longValue();
    }


}
