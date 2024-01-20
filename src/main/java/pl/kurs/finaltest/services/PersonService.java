package pl.kurs.finaltest.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.exceptions.*;
import pl.kurs.finaltest.models.*;
import pl.kurs.finaltest.models.commands.CreateEmployeePositionCommand;
import pl.kurs.finaltest.models.commands.CreateStudentCommand;
import pl.kurs.finaltest.repositories.EmployeePositionRepository;
import pl.kurs.finaltest.repositories.PersonRepository;
import pl.kurs.finaltest.specifications.PersonSpecification;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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