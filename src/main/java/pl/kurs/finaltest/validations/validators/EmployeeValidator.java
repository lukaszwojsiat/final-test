package pl.kurs.finaltest.validations.validators;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class EmployeeValidator implements EntityValidator {
    @Override
    public void validate(Map<String, Object> params, List<String> errors) {
        if (!params.containsKey("firstName") || ((String) params.get("firstName")).isBlank())
            errors.add("Pole FirstName nie może być puste");

        if (!params.containsKey("lastName") || ((String) params.get("lastName")).isBlank())
            errors.add("Pole LastName nie może być pusty");

        if (!params.containsKey("pesel") || !((String) params.get("pesel")).matches("\\d{11}"))
            errors.add("Pesel nieprawidłowy: " + params.get("pesel"));

        if (!params.containsKey("email") || !((String) params.get("email")).matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            errors.add("Email nieprawidłowy: " + params.get("email"));

        if (!params.containsKey("employmentStartDate") || !((LocalDate) params.get("employmentStartDate")).isBefore(LocalDate.now()))
            errors.add("EmploymentStartDate musi być zawierać się w przeszłości");

        if (!params.containsKey("actualPosition") || ((String) params.get("actualPosition")).isBlank())
            errors.add("Pole ActualPosition nie może być puste");

        if (!params.containsKey("actualSalary") || ((Integer) params.get("actualSalary")) < 0)
            errors.add("ActualSalary nie może być mniejszy od 0");
    }

    @Override
    public void validateDataFromCsv(String[] params, List<String> errors) {
        if (params.length != 10) {
            errors.add("Podano niewłaściwą ilość parametrów: " + params.length);
            return;
        }

        if (params[1].isBlank())
            errors.add("Pole FirstName nie może być puste");

        if (params[2].isBlank())
            errors.add("Pole LastName nie może być pusty");

        if (!params[3].matches("\\d{11}"))
            errors.add("Pesel nieprawidłowy: " + params[3]);

        if (!params[6].matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            errors.add("Email nieprawidłowy: " + params[6]);

        if (LocalDate.parse(params[7]).isAfter(LocalDate.now()))
            errors.add("EmploymentStartDate musi być zawierać się w przeszłości");

        if (params[8].isBlank())
            errors.add("Pole ActualPosition nie może być puste");

        if (Double.parseDouble(params[9]) < 0)
            errors.add("ActualSalary nie może być mniejszy od 0");
    }

}
