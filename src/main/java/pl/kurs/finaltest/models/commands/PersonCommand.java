package pl.kurs.finaltest.models.commands;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class PersonCommand {
    @NotEmpty
    private String name;
    @NotEmpty
    private Map<String, Object> parameters;
}