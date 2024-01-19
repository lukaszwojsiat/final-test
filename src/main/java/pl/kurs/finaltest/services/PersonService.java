package pl.kurs.finaltest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.exceptions.InvalidPositionDatesException;
import pl.kurs.finaltest.exceptions.ResourceNoFoundException;
import pl.kurs.finaltest.exceptions.WrongPersonInformationException;
import pl.kurs.finaltest.exceptions.WrongTypeOfPersonException;
import pl.kurs.finaltest.models.*;
import pl.kurs.finaltest.models.commands.CreateEmployeePositionCommand;
import pl.kurs.finaltest.repositories.EmployeePositionRepository;
import pl.kurs.finaltest.repositories.PersonRepository;
import pl.kurs.finaltest.specifications.PersonSpecification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    public List<EmployeePosition> getEmployeePositions(Long id) {
        Employee person = personRepository.getEmployeeWithPositions(id)
                .orElseThrow(() -> new WrongTypeOfPersonException("Podano zlego pracownika"));
        return new ArrayList<>(person.getPositions());
    }

    @Transactional
    public void addManyAsCsvFile(MultipartFile file) throws IOException {
        Stream<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines();
        lines.map(line -> line.split(","))
                .forEach(args -> save(convertToCorrectTypeOfPerson(args)));
    }


    public Person convertToCorrectTypeOfPerson(String[] arg) {
        switch (arg[0]) {
            case "Student" -> {
                return new Student(arg[1], arg[2], arg[3], Double.parseDouble(arg[4]), Double.parseDouble(arg[5]),
                        arg[6], arg[7], Integer.parseInt(arg[8]), arg[9], Double.parseDouble(arg[10]));
            }
            case "Retiree" -> {
                return new Retiree(arg[1], arg[2], arg[3], Double.parseDouble(arg[4]), Double.parseDouble(arg[5]),
                        arg[6], Double.parseDouble(arg[7]), Integer.parseInt(arg[8]));
            }
            case "Employee" -> {
                return new Employee(arg[1], arg[2], arg[3], Double.parseDouble(arg[4]), Double.parseDouble(arg[5]),
                        arg[6], LocalDate.parse(arg[7]), arg[8], Double.parseDouble(arg[9]));
            }
            default -> throw new WrongPersonInformationException("Podano bledne dana osobe: " + arg[0]);

        }
    }

    @Transactional
    public Employee addPosition(Long id, CreateEmployeePositionCommand command) {
        List<EmployeePosition> positions = employeePositionRepository.findAllByEmploymentStartDateBeforeAndEmploymentEndDateAfter(command.getEmploymentEndDate(),
                command.getEmploymentStartDate());
        if (positions.isEmpty()) {
            throw new InvalidPositionDatesException("Nie mozna przypisac nowego stanowiska w tym samym czasie: " + command.getEmploymentStartDate()
                    + " - " + command.getEmploymentEndDate());
        }
        Person person = getById(id);
        if (person.getClass() != Employee.class) {
            throw new WrongTypeOfPersonException("Podano zlego pracownika: " + person.getClass().getSimpleName());
        }
        Employee employee = (Employee) person;
        EmployeePosition employeePosition = new EmployeePosition(
                employee,
                command.getPositionName(),
                command.getSalary(),
                command.getEmploymentStartDate(),
                command.getEmploymentEndDate()
        );
        if (LocalDate.now().isAfter(employeePosition.getEmploymentStartDate()) && LocalDate.now().isBefore(employeePosition.getEmploymentEndDate()) || LocalDate.now().isEqual(employeePosition.getEmploymentStartDate())) {
            employee.setActualPosition(employeePosition.getPositionName());
            employee.setActualPosition(employeePosition.getPositionName());
            personRepository.save(employee);
        }
        employeePositionRepository.save(employeePosition);
        return employee;
    }
}