package pl.kurs.finaltest.models.commands;

import pl.kurs.finaltest.models.Person;

public interface CommandMapClassIdentifier {
    Class<? extends Person> classMapTo();
}
