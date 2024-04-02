package pl.kurs.finaltest.factories.entitycreators;

import org.springframework.stereotype.Service;
import pl.kurs.finaltest.exceptions.WrongTypeOfPersonException;
import pl.kurs.finaltest.models.Person;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PersonFactory {
    private final Map<String, PersonCreator> creators;

    public PersonFactory(Set<PersonCreator> creators) {
        this.creators = creators.stream().collect(Collectors.toMap(PersonCreator::getName, Function.identity()));
    }

    public Person createPersonToSave(String name, Map<String, Object> params) {
        creatorExist(name);
        return creators.get(name).createPersonToSave(params);
    }

    public Person createPersonToUpdate(String name, Map<String, Object> params) {
        creatorExist(name);
        return creators.get(name).createPersonToUpdate(params);
    }

    public Person createPersonFromCsv(String name, List<String> params){
        creatorExist(name);
        return creators.get(name).createPersonFromCsv(params);
    }

    private void creatorExist(String name) {
        if (!creators.containsKey(name))
            throw new WrongTypeOfPersonException("Nie znaleziono takiej osoby: " + name);
    }

}
