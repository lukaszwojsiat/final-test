package pl.kurs.finaltest.validations.validators;

import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;

public interface EntityValidator {
    void validate(Map<String, Object> params, List<String> errors);
    void validateDataFromCsv(String[] params, List<String> errors);
}
