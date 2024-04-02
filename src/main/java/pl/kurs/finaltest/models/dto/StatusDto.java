package pl.kurs.finaltest.models.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatusDto {
    private String status;

    public StatusDto(String status) {
        this.status = status;
    }
}
