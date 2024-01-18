package pl.kurs.finaltest.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.commands.CreatePersonCommand;
import pl.kurs.finaltest.models.commands.UpdatePersonCommand;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.models.dto.StatusDto;
import pl.kurs.finaltest.services.PersonService;
import pl.kurs.finaltest.specifications.PersonSpec;

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
    public ResponseEntity<List<PersonDto>> getAllBy(PersonSpec personSpec, Pageable pageable) {
        List<PersonDto> persons = personService.getAllBy(personSpec, pageable).stream()
                .map(p -> modelMapper.map(p, p.dtoClassMapTo()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(persons);
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

    @PutMapping
    public ResponseEntity<PersonDto> editPerson(@RequestBody @Valid UpdatePersonCommand dataToEdit) {
        Person personToEdit = modelMapper.map(dataToEdit, dataToEdit.classMapTo());
        Person editedPerson = personService.edit(personToEdit);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(editedPerson, editedPerson.dtoClassMapTo()));
    }
}
