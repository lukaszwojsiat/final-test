package pl.kurs.finaltest.exceptions.constraints;

import org.springframework.stereotype.Service;
import pl.kurs.finaltest.exceptions.ExceptionResponseDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class PersonUniquePeselConstraintErrorHandler implements ConstraintErrorHandler {
    @Override
    public ExceptionResponseDto mapToExceptionResponseDto() {
        return new ExceptionResponseDto(List.of("Pesel not unique"), "CONFLICT", Timestamp.from(Instant.now()));
    }

    @Override
    public String getConstraintName() {
        return "PUBLIC.UC_PERSON_PESEL";
    }
}
