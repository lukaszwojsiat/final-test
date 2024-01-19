package pl.kurs.finaltest.exceptions;

public class ResourceNoFoundException extends RuntimeException{
    public ResourceNoFoundException(String message) {
        super(message);
    }
}
