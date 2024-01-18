package pl.kurs.finaltest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.exceptions.WrongPersonInformationException;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Retiree;
import pl.kurs.finaltest.models.Student;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.repositories.PersonRepository;
import pl.kurs.finaltest.specifications.PersonSpec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;


@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
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
    public Page<Person> getAllBy(PersonSpec personSpec, Pageable pageable) {
        return personRepository.findAll(personSpec, pageable);
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
}