package pl.kurs.finaltest.services;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Retiree;
import pl.kurs.finaltest.models.Student;
import pl.kurs.finaltest.models.commands.CreatePersonCommand;
import pl.kurs.finaltest.models.commands.UpdatePersonCommand;
import pl.kurs.finaltest.repositories.PersonRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


@Service
public class PersonService {

    private final PersonRepository repository;
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_QUERY = "SELECT * FROM person WHERE ";

    public PersonService(PersonRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void findBy(Map<String, String> map) {
        String query = SQL_QUERY;
        boolean isMoreThanOneParameter = false;
        List<String> values = new ArrayList<>();
        int numberOfValues = 0;
        for (Map.Entry<String, String> entries : map.entrySet()) {
            String key = entries.getKey();
            String value = entries.getValue();
            values.add(value);
            if (numberOfValues > 0 && numberOfValues < map.size())
                query += " AND ";

            switch (key) {
//                case "firstName", "lastName", "pesel", "email", "employmentStartDate", "position",
//                        "completedUniversity", "fieldOfStudy" -> query += key + " = " + value;
//                case "salaryFrom", "studyYearFrom", "scholarshipFrom", "pensionFrom", "workedYearsFrom" -> query += key + " >= " + value;
//                case "salaryTo", "studyYearTo", "scholarshipTo", "pensionTo", "workedYearsTo" -> query += key + " <= " + value;
                case "first_Name", "lastName", "pesel", "email", "employmentStartDate", "position",
                        "completedUniversity", "fieldOfStudy" -> query += key + " = ?";
                case "salaryFrom", "studyYearFrom", "scholarshipFrom", "pensionFrom", "workedYearsFrom" -> query += key + " >= ?";
                case "salary", "studyYearTo", "scholarshipTo", "pensionTo", "workedYearsTo" -> query += key + " <= ?";
            }
            numberOfValues++;
        }
//        List<Map<String, Object>> maps = jdbcTemplate.query(query,  (resultSet, rowNum) -> {
        List<Person> maps = jdbcTemplate.query("SELECT * FROM person WHERE weight = 68", (resultSet, rowNum) -> {
            Person entity = null;
            if (resultSet.getString("dtype") == "Employee") {
                entity = new Employee();
                entity.setWeight(Double.parseDouble(resultSet.getString("weight")));
            }
            return entity;
        });
    }

    @Transactional
    public Person save(Person person) {
        return repository.save(person);
    }

    @Transactional
    public Person edit(Person personToEdit) {
        return repository.save(personToEdit);
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
            default -> {
                return null;
            }
        }
    }
}