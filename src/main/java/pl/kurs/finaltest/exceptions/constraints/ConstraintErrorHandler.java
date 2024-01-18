package pl.kurs.finaltest.exceptions.constraints;

import pl.kurs.finaltest.exceptions.ExceptionResponseDto;

public interface ConstraintErrorHandler {
    ExceptionResponseDto mapToExceptionResponseDto();

    String getConstraintName();
}
