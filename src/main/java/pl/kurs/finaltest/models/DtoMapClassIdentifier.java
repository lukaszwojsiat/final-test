package pl.kurs.finaltest.models;

import pl.kurs.finaltest.models.dto.PersonDto;

public interface DtoMapClassIdentifier {
    Class<? extends PersonDto> dtoClassMapTo();
}
