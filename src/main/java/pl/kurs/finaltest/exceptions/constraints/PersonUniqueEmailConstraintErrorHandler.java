package pl.kurs.finaltest.exceptions.constraints;

import org.springframework.stereotype.Service;
import pl.kurs.finaltest.exceptions.ExceptionResponseDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class PersonUniqueEmailConstraintErrorHandler implements ConstraintErrorHandler {
    @Override
    public ExceptionResponseDto mapToExceptionResponseDto() {
        return new ExceptionResponseDto(List.of("Email not unique"), "CONFLICT", Timestamp.from(Instant.now()));
    }

    @Override
    public String getConstraintName() {
        return "UC_PERSON_EMAIL";
    }
}
