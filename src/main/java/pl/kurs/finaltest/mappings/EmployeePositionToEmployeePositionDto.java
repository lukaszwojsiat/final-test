package pl.kurs.finaltest.mappings;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.models.EmployeePosition;
import pl.kurs.finaltest.models.dto.EmployeePositionDto;


@Service
public class EmployeePositionToEmployeePositionDto implements Converter<EmployeePosition, EmployeePositionDto> {
    @Override
    public EmployeePositionDto convert(MappingContext<EmployeePosition, EmployeePositionDto> mappingContext) {
        EmployeePosition source = mappingContext.getSource();
        EmployeePositionDto dto = new EmployeePositionDto(
                source.getId(),
                source.getEmployee().getId(),
                source.getPositionName(),
                source.getSalary(),
                source.getEmploymentStartDate(),
                source.getEmploymentEndDate());
        return dto;
    }
}
