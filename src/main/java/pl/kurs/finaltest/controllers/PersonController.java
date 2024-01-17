package pl.kurs.finaltest.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.commands.CreatePersonCommand;
import pl.kurs.finaltest.models.commands.UpdatePersonCommand;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.repositories.PersonRepository;
import pl.kurs.finaltest.services.PersonService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/persons")
public class PersonController {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final PersonService personService;

    public PersonController(PersonRepository personRepository, ModelMapper modelMapper, PersonService personService) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.personService = personService;
    }

//    @GetMapping(value = "/findPersonBy")
//    public ResponseEntity<List<Person>> getAllBy(@RequestParam(required = false, name = "firstName") String firstName,
//                                                 @RequestParam(required = false, name = "lastName") String lastName,
//                                                 @RequestParam(required = false, name = "pesel") String pesel,
//                                                 @RequestParam(required = false, name = "email") String email,
//                                                 @RequestParam(required = false, name = "employmentStartDate") LocalDate employmentStartDate,
//                                                 @RequestParam(required = false, name = "position") String position,
//                                                 @RequestParam(required = false, name = "salaryFrom") Double salaryFrom,
//                                                 @RequestParam(required = false, name = "salaryTo") Double salaryTo,
//                                                 @RequestParam(required = false, name = "completedUniversity") String completedUniversity,
//                                                 @RequestParam(required = false, name = "studyYearFrom") Integer studyYearFrom,
//                                                 @RequestParam(required = false, name = "studyYearTo") Integer studyYearTo,
//                                                 @RequestParam(required = false, name = "fieldOfStudy") String fieldOfStudy,
//                                                 @RequestParam(required = false, name = "scholarshipFrom") Double scholarshipFrom,
//                                                 @RequestParam(required = false, name = "scholarshipTo") Double scholarshipTo,
//                                                 @RequestParam(required = false, name = "pensionFrom") Double pensionFrom,
//                                                 @RequestParam(required = false, name = "pensionTo") Double pensionTo,
//                                                 @RequestParam(required = false, name = "workedYearsFrom") Integer workedYearsFrom,
//                                                 @RequestParam(required = false, name = "workedYearsTo") Integer workedYearsTo) {
//        List<Person> persons = personRepository.findAllByFirstName(firstName).stream()
//                .map(x -> modelMapper.map(x, Person.class))
//                .collect(Collectors.toList());
//        return ResponseEntity.status(HttpStatus.OK).body(persons);
//    }

    @GetMapping(value = "/findPersonBy")
    public ResponseEntity<String> getAllBy(@RequestParam Map<String, String> map) {
        personService.findBy(map);
        return ResponseEntity.status(HttpStatus.OK).body(map.get("dupa"));
    }

//    @GetMapping(value = "/findPersonBy", params = {"firstName", "lastName"})
//    public ResponseEntity<List<Person>> getAllByFirstName(@RequestParam(required = false, name = "firstName") String firstName, @RequestParam(required = false, name = "lastName") String lastName) {
//        List<Person> persons = personRepository.findAllByFirstName(firstName).stream()
//                .map(x -> modelMapper.map(x, Person.class))
//                .collect(Collectors.toList());
//        return ResponseEntity.status(HttpStatus.OK).body(persons);
//    }

//    @PostMapping
//    public ResponseEntity<Person> createPerson(@RequestBody CreatePersonCommand createPersonCommand) {
//    }

    @GetMapping(value = "/findPersonBy", params = "lastName")
    public ResponseEntity<List<Person>> getAllByLastName(@RequestParam("lastName") String lastName) {
        List<Person> persons = personRepository.findAllByFirstName(lastName).stream()
                .map(x -> modelMapper.map(x, Person.class))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(persons);
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody CreatePersonCommand personToAdd) {
        Person person = modelMapper.map(personToAdd, personToAdd.classMapTo());
        Person savedPerson = personService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(savedPerson, savedPerson.dtoClassMapTo()));
    }


    @PostMapping("/upload")
    public ResponseEntity addManyAsCsvFile(@RequestParam("file") MultipartFile file) throws IOException {
        personService.addManyAsCsvFile(file);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PersonDto> editPerson(@RequestBody UpdatePersonCommand dataToEdit) {
        Person personToEdit = modelMapper.map(dataToEdit, dataToEdit.classMapTo());
        Person editedPerson = personService.edit(personToEdit);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(editedPerson, editedPerson.dtoClassMapTo()));
    }
}
