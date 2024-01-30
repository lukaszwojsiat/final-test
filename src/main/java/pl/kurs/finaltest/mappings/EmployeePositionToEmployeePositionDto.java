package pl.kurs.finaltest.mappings;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.models.EmployeePosition;
import pl.kurs.finaltest.models.dto.EmployePositionDto;


@Service
public class EmployeePositionToEmployeePositionDto implements Converter<EmployeePosition, EmployePositionDto> {
    @Override
    public EmployePositionDto convert(MappingContext<EmployeePosition, EmployePositionDto> mappingContext) {
        EmployeePosition source = mappingContext.getSource();
        EmployePositionDto dto = new EmployePositionDto(
                source.getId(),
                source.getEmployee().getId(),
                source.getPositionName(),
                source.getSalary(),
                source.getEmploymentStartDate(),
                source.getEmploymentEndDate());
        return dto;
    }
}
