package pl.kurs.finaltest.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.EmployeePosition;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.commands.CreateEmployeePositionCommand;
import pl.kurs.finaltest.models.commands.CreatePersonCommand;
import pl.kurs.finaltest.models.commands.UpdatePersonCommand;
import pl.kurs.finaltest.models.dto.*;
import pl.kurs.finaltest.services.PersonService;
import pl.kurs.finaltest.specifications.PersonSpecification;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/persons")
public class PersonController {
    private final ModelMapper modelMapper;
    private final PersonService personService;

    public PersonController(ModelMapper modelMapper, PersonService personService) {
        this.modelMapper = modelMapper;
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAllPersonsBy(PersonSpecification personSpecification, Pageable pageable) {
        List<PersonDto> persons = personService.getAllBy(personSpecification, pageable).stream()
                .map(p -> modelMapper.map(p, p.dtoClassMapTo()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(persons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable Long id) {
        Person person = personService.getById(id);
        return ResponseEntity.ok(modelMapper.map(person, person.dtoClassMapTo()));
    }

    @GetMapping("/{id}/positions")
    public ResponseEntity<List<EmployePositionDto>> getEmployeePositions(@PathVariable Long id) {
        List<EmployeePosition> positions = personService.getEmployeePositions(id);
        return ResponseEntity.ok(positions.stream()
                .map(ep ->modelMapper.map(positions, EmployePositionDto.class))
                .collect(Collectors.toList())
        );
    }


    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody @Valid CreatePersonCommand personToAdd) {
        Person person = modelMapper.map(personToAdd, personToAdd.classMapTo());
        Person savedPerson = personService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(savedPerson, savedPerson.dtoClassMapTo()));
    }


    @PostMapping("/upload")
    public ResponseEntity<StatusDto> addManyAsCsvFile(@RequestParam("file") MultipartFile file) throws IOException {
        personService.addManyAsCsvFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new StatusDto("Osoby z pliku csv zostaly dodane."));
    }

    @PostMapping("/{id}/position")
    public ResponseEntity<EmployeeDto> addPositionToEmployee(@PathVariable Long id, @RequestBody CreateEmployeePositionCommand command) {
        Employee employee = personService.addPosition(id, command);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(employee, EmployeeDto.class));
    }

    @PutMapping
    public ResponseEntity<PersonDto> editPerson(@RequestBody @Valid UpdatePersonCommand dataToEdit) {
        Person personToEdit = modelMapper.map(dataToEdit, dataToEdit.classMapTo());
        Person editedPerson = personService.edit(personToEdit);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(editedPerson, editedPerson.dtoClassMapTo()));
    }

}
