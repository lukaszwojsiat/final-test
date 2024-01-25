package pl.kurs.finaltest.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExceptionResponseDto {
    private List<String> errorsMessages;
    private String errorCode;
    private Timestamp timestamp;
}
