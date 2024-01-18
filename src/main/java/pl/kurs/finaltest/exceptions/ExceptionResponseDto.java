package pl.kurs.finaltest.exceptions;

import java.sql.Timestamp;
import java.util.List;

public class ExceptionResponseDto {
    private List<String> errorsMessages;
    private String errorCode;
    private Timestamp timestamp;

    public ExceptionResponseDto(List<String> errorsMessages, String errorCode, Timestamp timestamp) {
        this.errorsMessages = errorsMessages;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public List<String> getErrorsMessages() {
        return errorsMessages;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
