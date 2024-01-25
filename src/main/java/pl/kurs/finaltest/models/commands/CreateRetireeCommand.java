package pl.kurs.finaltest.models.commands;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.finaltest.annotations.PersonCommandSubType;
import pl.kurs.finaltest.models.Retiree;

@Getter
@Setter
@PersonCommandSubType("Retiree")
public class CreateRetireeCommand extends CreatePersonCommand {
    @NotNull
    private Double pension;
    @NotNull
    private Integer workedYears;

    @Override
    public Class<? extends Retiree> classMapTo() {
        return Retiree.class;
    }
}
