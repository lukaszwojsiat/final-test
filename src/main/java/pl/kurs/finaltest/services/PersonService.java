package pl.kurs.finaltest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.exceptions.InvalidPositionDatesException;
import pl.kurs.finaltest.exceptions.ResourceNoFoundException;
import pl.kurs.finaltest.exceptions.WrongTypeOfPersonException;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.EmployeePosition;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.commands.CreateEmployeePositionCommand;
import pl.kurs.finaltest.repositories.EmployeePositionRepository;
import pl.kurs.finaltest.repositories.PersonRepository;
import pl.kurs.finaltest.specifications.PersonSpecification;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final EmployeePositionRepository employeePositionRepository;

    public PersonService(PersonRepository personRepository, EmployeePositionRepository employeePositionRepository) {
        this.personRepository = personRepository;
        this.employeePositionRepository = employeePositionRepository;
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
    public Person edit(Person personToEdit) {
        return personRepository.save(personToEdit);
    }

    @Transactional(readOnly = true)
    public Person getById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new ResourceNoFoundException("Nie znaleziono osoby o id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Person> getAllBy(PersonSpecification personSpecification, Pageable pageable) {
        return personRepository.findAll(personSpecification, pageable);
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeWithPositions(Long id) {
        return personRepository.getEmployeeWithPositions(id)
                .orElseThrow(() -> new WrongTypeOfPersonException("Podano zlego pracownika"));
    }

    @Transactional
    public Employee addPositionToEmployee(Long id, CreateEmployeePositionCommand command) {
        Employee employee = getEmployeeWithPositions(id);
        List<EmployeePosition> positions = employee.getPositions().stream()
                .filter(p -> p.getEmploymentStartDate().isBefore(command.getEmploymentEndDate().plusDays(1L)) && p.getEmploymentEndDate().isAfter(command.getEmploymentStartDate().minusDays(1L)))
                .collect(Collectors.toList());
        if (!positions.isEmpty()) {
            throw new InvalidPositionDatesException("Nie mozna przypisac nowego stanowiska w tym samym czasie co inne stanowisko: " + command.getEmploymentStartDate()
                    + " - " + command.getEmploymentEndDate());
        }
        EmployeePosition employeePosition = new EmployeePosition(
                employee,
                command.getPositionName(),
                command.getSalary(),
                command.getEmploymentStartDate(),
                command.getEmploymentEndDate()
        );
        if ((LocalDate.now().isAfter(employeePosition.getEmploymentStartDate().minusDays(1L))) && (LocalDate.now().isBefore(employeePosition.getEmploymentEndDate().plusDays(1L)))) {
            employee.setActualPosition(employeePosition.getPositionName());
            employee.setActualPosition(employeePosition.getPositionName());
            personRepository.save(employee);
        }
        employeePositionRepository.save(employeePosition);
        return employee;
    }
}