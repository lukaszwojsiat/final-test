package pl.kurs.finaltest.exceptions;

public class MissingUpdateException extends RuntimeException {
    public MissingUpdateException(String message) {
        super(message);
    }
}
