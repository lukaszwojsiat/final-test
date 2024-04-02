package pl.kurs.finaltest.factories.sqlcreators;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.exceptions.WrongTypeOfPersonException;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SqlFactory {
    private final JdbcTemplate jdbcTemplate;
    private final Map<String, PersonSqlCreator> creators;

    public SqlFactory(JdbcTemplate jdbcTemplate, Set<PersonSqlCreator> jdbcTemplateUpdate) {
        this.jdbcTemplate = jdbcTemplate;
        creators = jdbcTemplateUpdate.stream().collect(Collectors.toMap(PersonSqlCreator::getName, Function.identity()));
    }

    public void jdbcTemplateUpdate(String[] args) {
        creatorExist(args[0]);
        jdbcTemplate.update(creators.get(args[0]).getSqlQueryToInsert(), args, creators.get(args[0]).getTypesToInsert());
    }

    private void creatorExist(String name) {
        if (!creators.containsKey(name))
            throw new WrongTypeOfPersonException("Nie znaleziono takiej osoby: " + name);
    }
}
