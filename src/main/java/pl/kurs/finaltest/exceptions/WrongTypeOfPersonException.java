package pl.kurs.finaltest.exceptions;

public class WrongTypeOfPersonException extends RuntimeException {
    public WrongTypeOfPersonException(String message) {
        super(message);
    }
}
