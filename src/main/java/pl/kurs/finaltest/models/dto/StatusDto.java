package pl.kurs.finaltest.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class StatusDto {
    private String status;

    public StatusDto(String status) {
        this.status = status;
    }
}
