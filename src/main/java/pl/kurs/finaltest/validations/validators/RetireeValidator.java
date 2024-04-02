package pl.kurs.finaltest.validations.validators;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RetireeValidator implements EntityValidator {
    @Override
    public void validate(Map<String, Object> params, List<String> errors) {
        if (!params.containsKey("firstName") || ((String) params.get("firstName")).isBlank())
            errors.add("Pole FirstName nie może być puste");

        if (!params.containsKey("lastName") || ((String) params.get("lastName")).isBlank())
            errors.add("Pole LastName nie może być puste");

        if (!params.containsKey("pesel") || !((String) params.get("pesel")).matches("\\d{11}"))
            errors.add("Pesel nieprawidłowy: " + params.get("pesel"));

        if (!params.containsKey("email") || !((String) params.get("email")).matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            errors.add("Email nieprawidłowy: " + params.get("email"));

        if (!params.containsKey("pension") || ((Double) params.get("pension")) < 0)
            errors.add("Pension nie może być mniejszy od 0");

        if (!params.containsKey("workedYears") || ((Integer) params.get("workedYears")) < 0)
            errors.add("WorkedYears nie może być mniejszy od 0");
    }

    @Override
    public void validateDataFromCsv(String[] params, List<String> errors) {
        if (params.length != 9) {
            errors.add("Podano niewłaściwą ilość parametrów: " + params.length);
            return;
        }

        if (params[1].isBlank())
            errors.add("Pole FirstName nie może być puste");

        if (params[2].isBlank())
            errors.add("Pole LastName nie może być puste");

        if (!params[3].matches("\\d{11}"))
            errors.add("Pesel nieprawidłowy: " + params[3]);

        if (!params[6].matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            errors.add("Email nieprawidłowy: " + params[6]);

        if (Double.parseDouble(params[7]) < 0)
            errors.add("Pension nie może być mniejszy od 0");

        if (Integer.parseInt(params[8]) < 0)
            errors.add("WorkedYears nie może być mniejszy od 0");
    }
}
