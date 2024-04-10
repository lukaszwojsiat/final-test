package pl.kurs.finaltest.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.finaltest.models.EmployeePosition;
import pl.kurs.finaltest.models.commands.CreateEmployeePositionCommand;
import pl.kurs.finaltest.models.dto.EmployeePositionDto;
import pl.kurs.finaltest.services.EmployeePositionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/employee-positions")
@Validated
public class EmployeePositionController {

    private final ModelMapper modelMapper;
    private final EmployeePositionService employeePositionService;

    public EmployeePositionController(ModelMapper modelMapper, EmployeePositionService employeePositionService) {
        this.modelMapper = modelMapper;
        this.employeePositionService = employeePositionService;
    }

    @GetMapping("/by-employee/{id}")
    public ResponseEntity<List<EmployeePositionDto>> getEmployeePositionsByEmployeeId(@PathVariable Long id) {
        List<EmployeePositionDto> employeePositionDtos = employeePositionService.getEmployeePositionsByEmployeeId(id).stream()
                .map(ep -> modelMapper.map(ep, EmployeePositionDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeePositionDtos);
    }


    @PostMapping
    public ResponseEntity<EmployeePositionDto> createEmployeePosition(@RequestBody @Valid CreateEmployeePositionCommand command) {
        EmployeePosition employeePositionToSave = modelMapper.map(command, EmployeePosition.class);
        EmployeePosition savedEmployeePosition = employeePositionService.createEmployeePosition(command.getEmployeeId(), employeePositionToSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(savedEmployeePosition, EmployeePositionDto.class));
    }
}
