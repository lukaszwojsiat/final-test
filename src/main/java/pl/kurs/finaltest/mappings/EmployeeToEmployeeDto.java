package pl.kurs.finaltest.mappings;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.controllers.PersonController;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.dto.EmployeeDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeToEmployeeDto implements Converter<Employee, EmployeeDto> {
    @Override
    public EmployeeDto convert(MappingContext<Employee, EmployeeDto> mappingContext) {
        Employee source = mappingContext.getSource();
        EmployeeDto dto = new EmployeeDto(
                source.getId(),
                source.getFirstName(),
                source.getLastName(),
                source.getPesel(),
                source.getHeight(),
                source.getWeight(),
                source.getEmail(),
                source.getVersion(),
                source.getEmploymentStartDate(),
                source.getActualPosition(),
                source.getActualSalary());

        dto.add(linkTo(methodOn(PersonController.class).getEmployeePositions(source.getId())).withRel("his-positions"));

        return dto;
    }
}
