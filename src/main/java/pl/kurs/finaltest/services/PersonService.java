package pl.kurs.finaltest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.exceptions.MissingUpdateException;
import pl.kurs.finaltest.exceptions.ResourceNoFoundException;
import pl.kurs.finaltest.exceptions.WrongPersonInformationException;
import pl.kurs.finaltest.exceptions.WrongTypeOfPersonException;
import pl.kurs.finaltest.factories.specifications.PersonSpecificationFactory;
import pl.kurs.finaltest.factories.sqlcreators.SqlFactory;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.ImportStatus;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Status;
import pl.kurs.finaltest.models.commands.PersonCommand;
import pl.kurs.finaltest.repositories.PersonRepository;
import pl.kurs.finaltest.validations.validators.EntityValidator;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;


@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonSpecificationFactory personSpecificationFactory;
    private final SqlFactory sqlFactory;
    private final ImportService importService;
    private final Map<String, EntityValidator> validators;

    public PersonService(PersonRepository personRepository, PersonSpecificationFactory personSpecificationFactory, SqlFactory sqlFactory, ImportService importService, Map<String, EntityValidator> validators) {
        this.personRepository = personRepository;
        this.personSpecificationFactory = personSpecificationFactory;
        this.sqlFactory = sqlFactory;
        this.importService = importService;
        this.validators = validators;
    }


    @Transactional
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public void saveAll(List<Person> persons) {
        personRepository.saveAll(persons);
    }

    @Transactional
    public Person edit(Long id, Person personToEdit) {
        Person person = getById(id);
        if (person.getId() != personToEdit.getId()) {
            throw new WrongPersonInformationException("Podana osoba do edycji ma inny id: " + personToEdit.getId());
        } else if (!person.getClass().equals(personToEdit.getClass()))
            throw new WrongTypeOfPersonException("Podano zły typ do edycji: " + personToEdit.getClass().getSimpleName() +
                    ", typ z bazy danych o danym id: " + person.getClass().getSimpleName());
        Person updatedPerson = personRepository.saveAndFlush(personToEdit);
        if (updatedPerson.getVersion() == personToEdit.getVersion())
            throw new MissingUpdateException("Osoba nie została zaaktualizowana, sprawdź dane do edycji.");
        return updatedPerson;
    }

    @Transactional(readOnly = true)
    public Person getById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new ResourceNoFoundException("Nie znaleziono osoby o id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Person> getAllBy(Map<String, String> filterParameters, Pageable pageable) {
        Specification<Person> spec = Specification.where(null);
        if (filterParameters != null) {
            Set<Map.Entry<String, String>> entries = filterParameters.entrySet();
            for (Map.Entry<String, String> parameters : entries) {
                spec = spec.and(personSpecificationFactory.getSpecification(parameters.getKey(), parameters.getValue()));
            }
        }
        return personRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeWithPositions(Long id) {
        return personRepository.getEmployeeWithPositions(id)
                .orElseThrow(() -> new WrongTypeOfPersonException("Nie znaleziono pracownika"));
    }

    @Transactional
    public Employee getEmployeeWithPositionsWithLocking(Long id) {
        return personRepository.getEmployeeWithPositionsWithLocking(id).orElseThrow(() -> new ResourceNoFoundException("Nie znaleziono pracownika"));
    }

    @Async("threadPoolTaskExecutor")
    @Transactional
    public void addManyAsCsvFile(Stream<String> lines, ImportStatus importStatus) {
        importStatus.setStatus(Status.PROCESSING);
        importStatus.setStartData(LocalDate.now());
        importService.save(importStatus);

        long s1 = System.currentTimeMillis();

        try {
            lines.map(line -> line.split(","))
                    .forEach(args -> {
                        validateToAddAsCsv(args);
                        String[] arguments = Arrays.copyOf(args, args.length + 1);
                        arguments[arguments.length - 1] = "0";  //Add column 'version'
                        sqlFactory.jdbcTemplateUpdate(arguments);
                        importStatus.setProcessedRows(importStatus.getProcessedRows() + 1);
                    });
        } catch (Exception e) {
            importStatus.setStatus(Status.INTERRUPTED);
            importService.save(importStatus);
            throw e;
        }

        importStatus.setStatus(Status.DONE);
        importService.save(importStatus);
        long s2 = System.currentTimeMillis();
        System.out.println(s2 - s1);
    }

    public void validateToSave(PersonCommand personCommand) {
        String validatorName = personCommand.getName().toLowerCase(Locale.ROOT) + "Validator";
        if (!validators.containsKey(validatorName))
            throw new WrongTypeOfPersonException("Nie znaleziono takiej osoby: " + personCommand.getName());

        List<String> errors = new ArrayList<>();
        validators.get(validatorName).validate(personCommand.getParameters(), errors);
        if (!errors.isEmpty())
            throw new WrongPersonInformationException(errors.toString());
    }

    public void validateToAddAsCsv(String[] params) {
        if (params.length == 0)
            throw new WrongPersonInformationException("Nie podano parametrów");
        String validatorName = params[0].toLowerCase(Locale.ROOT) + "Validator";
        if (!validators.containsKey(validatorName))
            throw new WrongTypeOfPersonException("Nie znaleziono takiej osoby: " + params[0]);

        List<String> errors = new ArrayList<>();
        validators.get(validatorName).validateDataFromCsv(params, errors);
        if (!errors.isEmpty())
            throw new WrongPersonInformationException(errors.toString());
    }
}