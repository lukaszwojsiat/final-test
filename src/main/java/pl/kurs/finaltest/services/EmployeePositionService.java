package pl.kurs.finaltest.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.exceptions.InvalidPositionDatesException;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.EmployeePosition;
import pl.kurs.finaltest.repositories.EmployeePositionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeePositionService {
    private final EmployeePositionRepository repository;
    private final PersonService personService;
    private final ModelMapper modelMapper;

    public EmployeePositionService(EmployeePositionRepository repository, PersonService personService, ModelMapper modelMapper) {
        this.repository = repository;
        this.personService = personService;
        this.modelMapper = modelMapper;
    }


    @Transactional(readOnly = true)
    public List<EmployeePosition> getEmployeePositionsByEmployeeId(Long id) {
        return repository.getEmployeePositionByEmployeeId(id);
    }

    @Transactional
    public EmployeePosition saveAndSetNewEmployeeProperties(EmployeePosition employeePosition) {
        if ((LocalDate.now().isAfter(employeePosition.getEmploymentStartDate().minusDays(1L))) && (LocalDate.now().isBefore(employeePosition.getEmploymentEndDate().plusDays(1L)))) {
            employeePosition.getEmployee().setActualPosition(employeePosition.getPositionName());
            employeePosition.getEmployee().setActualSalary(employeePosition.getSalary());
            personService.save(employeePosition.getEmployee());
        }
        return repository.save(employeePosition);
    }

    public void validatePositionDates(EmployeePosition employeePosition) {
        List<EmployeePosition> positions = employeePosition.getEmployee().getPositions().stream()
                .filter(p -> p.getEmploymentStartDate().isBefore(employeePosition.getEmploymentEndDate().plusDays(1L)) && p.getEmploymentEndDate().isAfter(employeePosition.getEmploymentStartDate().minusDays(1L)))
                .collect(Collectors.toList());

        if (!positions.isEmpty()) {
            throw new InvalidPositionDatesException("Nie mozna przypisac nowego stanowiska w tym samym czasie co inne stanowisko: " + employeePosition.getEmploymentStartDate()
                    + " - " + employeePosition.getEmploymentEndDate());
        }
    }

    @Transactional
    public EmployeePosition createEmployeePosition(Long employeeId, EmployeePosition employeePosition) {
        Employee employee = personService.getEmployeeWithPositionsWithLocking(employeeId);
        employeePosition.setEmployee(employee);
        validatePositionDates(employeePosition);
        return saveAndSetNewEmployeeProperties(employeePosition);
    }
}
