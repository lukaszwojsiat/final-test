package pl.kurs.finaltest.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.exceptions.EmptyFileException;
import pl.kurs.finaltest.factories.entitycreators.PersonFactory;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.ImportStatus;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Status;
import pl.kurs.finaltest.models.commands.CreateEmployeePositionCommand;
import pl.kurs.finaltest.models.commands.PersonCommand;
import pl.kurs.finaltest.models.dto.EmployeeDto;
import pl.kurs.finaltest.models.dto.EmployeePositionDto;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.models.dto.StatusDto;
import pl.kurs.finaltest.services.ImportService;
import pl.kurs.finaltest.services.PersonService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/api/persons")
public class PersonController {
    private final ModelMapper modelMapper;
    private final PersonService personService;
    private final ImportService importService;
    private final PersonFactory personFactory;

    public PersonController(ModelMapper modelMapper, PersonService personService, ImportService importService, PersonFactory personFactory) {
        this.modelMapper = modelMapper;
        this.personService = personService;
        this.importService = importService;
        this.personFactory = personFactory;
    }


    @GetMapping()
    public ResponseEntity<Page<PersonDto>> getAllPersonsBy(@RequestParam(required = false) Map<String, String> filterParameters, Pageable pageable) {
        Page<PersonDto> persons = personService.getAllBy(filterParameters, pageable)
                .map(person -> modelMapper.map(person, person.dtoClassMapTo()));
        return ResponseEntity.status(HttpStatus.OK).body(persons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable Long id) {
        Person person = personService.getById(id);
        return ResponseEntity.ok(modelMapper.map(person, person.dtoClassMapTo()));
    }

    @GetMapping("/employee/{id}/positions")
    public ResponseEntity<List<EmployeePositionDto>> getEmployeePositions(@PathVariable Long id) {
        List<EmployeePositionDto> employeePositionDtos = personService.getEmployeeWithPositions(id).getPositions().stream()
                .map(ep -> modelMapper.map(ep, EmployeePositionDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeePositionDtos);
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody @Valid PersonCommand dataToAdd) {
        personService.validateToSave(dataToAdd);
        Person person = personFactory.createPersonToSave(dataToAdd.getName(), dataToAdd.getParameters());
        Person savedPerson = personService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(savedPerson, savedPerson.dtoClassMapTo()));
    }

    @PostMapping("/upload")
    public ResponseEntity<StatusDto> addManyAsCsvFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new EmptyFileException("Nie dodano pliku do importu");
        }
        ImportStatus importStatus = importService.save(new ImportStatus(Status.CREATED, LocalDate.now(), null, 0L));
        Stream<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines();
        personService.addManyAsCsvFile(lines, importStatus);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StatusDto("Rozpoczeto import o id: " + importStatus.getId()));
    }

    @GetMapping("/upload/status/{id}")
    @Transactional
    public ResponseEntity<ImportStatus> getImportStatus(@PathVariable Long id) {
        ImportStatus importStatus = importService.findById(id);
        return ResponseEntity.ok(importStatus);
    }

    @PostMapping("/employee/{id}/position")
    public ResponseEntity<EmployeeDto> addPositionToEmployee(@PathVariable Long id, @RequestBody CreateEmployeePositionCommand command) {
        Employee employee = personService.addPositionToEmployee(id, command);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(employee, EmployeeDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> editPerson(@PathVariable Long id, @RequestBody @Valid PersonCommand dataToEdit) {
        personService.validateToSave(dataToEdit);
        Person personToEdit = personFactory.createPersonToUpdate(dataToEdit.getName(), dataToEdit.getParameters());
        Person editedPerson = personService.edit(id, personToEdit);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(editedPerson, editedPerson.dtoClassMapTo()));
    }

}
