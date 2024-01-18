package pl.kurs.finaltest.exceptions;

public class WrongPersonInformationException extends RuntimeException {
    public WrongPersonInformationException(String message) {
        super(message);
    }
}
