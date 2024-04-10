package pl.kurs.finaltest.exceptions.handlers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.finaltest.exceptions.*;
import pl.kurs.finaltest.exceptions.constraints.ConstraintErrorHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Map<String, ConstraintErrorHandler> constraintErrorHandlerMap;

    public GlobalExceptionHandler(Set<ConstraintErrorHandler> handlers) {
        this.constraintErrorHandlerMap = handlers.stream().collect(Collectors.toMap(ConstraintErrorHandler::getConstraintName, Function.identity()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                e.getFieldErrors().stream()
                        .map(fe -> "Field: " + fe.getField() + " / rejected value: '" + fe.getRejectedValue() + "' / message: " + fe.getDefaultMessage())
                        .collect(Collectors.toList()),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDto);
    }

    @ExceptionHandler({WrongPersonInformationException.class})
    public ResponseEntity<ExceptionResponseDto> handleWrongPersonInformationException(WrongPersonInformationException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(e.getMessage()),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDto);
    }

    @ExceptionHandler({InvalidPositionDatesException.class})
    public ResponseEntity<ExceptionResponseDto> handleInvalidPositionDatesException(InvalidPositionDatesException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(e.getMessage()),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDto);
    }

    @ExceptionHandler({MissingUpdateException.class})
    public ResponseEntity<ExceptionResponseDto> handleMissingUpdateException(MissingUpdateException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(e.getMessage()),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDto);
    }

    @ExceptionHandler({EmptyFileException.class})
    public ResponseEntity<ExceptionResponseDto> handleEmptyFileException(EmptyFileException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(e.getMessage()),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDto);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(e.getMessage()),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDto);
    }

    @ExceptionHandler({WrongTypeOfPersonException.class})
    public ResponseEntity<ExceptionResponseDto> handleWrongTypeOfPersonException(WrongTypeOfPersonException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(e.getMessage()),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDto);
    }

    @ExceptionHandler({ResourceNoFoundException.class})
    public ResponseEntity<ExceptionResponseDto> handleResourceNoFoundException(ResourceNoFoundException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of(e.getMessage()),
                "NOT_FOUND",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponseDto);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ExceptionResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        ExceptionResponseDto exceptionResponseDto = constraintErrorHandlerMap.get(e.getMessage()
                .substring(e.getMessage().indexOf("PUBLIC"), e.getMessage().indexOf("INDEX_8") - 1))
                .mapToExceptionResponseDto();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDto);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ExceptionResponseDto> handleConstraintViolationException(ConstraintViolationException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDto);
    }

    @ExceptionHandler({StaleObjectStateException.class})
    public ResponseEntity<ExceptionResponseDto> handleStaleObjectStateException(StaleObjectStateException e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                List.of("Data version different"),
                "CONFLICT",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDto);
    }

}
