package pl.kurs.finaltest.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.finaltest.FinalTestApplication;
import pl.kurs.finaltest.models.commands.CreateEmployeePositionCommand;
import pl.kurs.finaltest.models.dto.EmployeePositionDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FinalTestApplication.class)
@AutoConfigureMockMvc
class EmployeePositionControllerTestIT {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenGetPositionsRequest_whenRetrieveEmployeePositions_thenIsOk() throws Exception {
        //when
        //then
        mvc.perform(MockMvcRequestBuilders.get("/api/employee-positions/by-employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].positionName").value("Architekt"))
                .andExpect(jsonPath("$[0].salary").value(5000))
                .andExpect(jsonPath("$[0].employmentStartDate").value("2010-08-11"))
                .andExpect(jsonPath("$[0].employmentEndDate").value("2014-01-31"))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void givenGetPositionsRequestWithWrongId_thenIsOK() throws Exception {
        //when
        //then
        String response = mvc.perform(MockMvcRequestBuilders.get("/api/employee-positions/by-employee/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployeePositionDto> positions = objectMapper.readValue(response, new TypeReference<>() {
        });
        assertTrue(positions.isEmpty());
    }

    @WithMockUser(roles = "EMPLOYEE")
    @Test
    void givenPostPositionRequest_whenRequestExecuted_thenIsCreated() throws Exception {
        //given
        String responseBeforePost = mvc.perform(MockMvcRequestBuilders.get("/api/employee-positions/by-employee/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployeePositionDto> positionsBeforePost = objectMapper.readValue(responseBeforePost, new TypeReference<>() {
        });
        CreateEmployeePositionCommand command = new CreateEmployeePositionCommand(
                1L,
                "Projektant",
                2500,
                LocalDate.of(2009, 1, 1),
                LocalDate.of(2010, 7, 31)
        );
        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/employee-positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        String responseAfterPost = mvc.perform(MockMvcRequestBuilders.get("/api/employee-positions/by-employee/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployeePositionDto> positionsAfterPost = objectMapper.readValue(responseAfterPost, new TypeReference<>() {
        });
        EmployeePositionDto positionDto = positionsAfterPost.stream().filter(p -> p.getPositionName().equals("Projektant")).findAny().orElseThrow();
        assertNotEquals(positionsBeforePost.size(), positionsAfterPost.size());
        assertEquals(positionDto.getEmployeeId(), command.getEmployeeId());
        assertEquals(positionDto.getPositionName(), command.getPositionName());
        assertEquals(positionDto.getSalary(), command.getSalary());
        assertEquals(positionDto.getEmploymentStartDate(), command.getEmploymentStartDate());
        assertEquals(positionDto.getEmploymentEndDate(), command.getEmploymentEndDate());
    }

    @WithMockUser(roles = "EMPLOYEE")
    @Test
    void givenPostPositionRequestWithInvalidProperties_thenIsBadRequest() throws Exception {
        //given
        String responseBeforePost = mvc.perform(MockMvcRequestBuilders.get("/api/employee-positions/by-employee/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployeePositionDto> positionsBeforePost = objectMapper.readValue(responseBeforePost, new TypeReference<>() {
        });
        CreateEmployeePositionCommand command = new CreateEmployeePositionCommand(
                null,
                " ",
                2500,
                LocalDate.of(2009, 1, 1),
                LocalDate.of(2010, 7, 31)
        );

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/employee-positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        //then
        String responseAfterPost = mvc.perform(MockMvcRequestBuilders.get("/api/employee-positions/by-employee/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployeePositionDto> positionsAfterPost = objectMapper.readValue(responseAfterPost, new TypeReference<>() {
        });
        assertEquals(positionsBeforePost.size(), positionsAfterPost.size());
    }

    @WithMockUser(roles = "EMPLOYEE")
    @Test
    void givenPostWithWrongPositionRequest_whenRequestExecuted_thenBadRequest() throws Exception {
        //given
        String responseBeforePost = mvc.perform(MockMvcRequestBuilders.get("/api/employee-positions/by-employee/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployeePositionDto> positionsBeforePost = objectMapper.readValue(responseBeforePost, new TypeReference<>() {
        });
        CreateEmployeePositionCommand command = new CreateEmployeePositionCommand(
                1L,
                "Malarz",
                2500,
                LocalDate.of(2010, 8, 11),
                LocalDate.of(2013, 5, 12)
        );
        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/employee-positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        String responseAfterPost = mvc.perform(MockMvcRequestBuilders.get("/api/employee-positions/by-employee/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployeePositionDto> positionsAfterPost = objectMapper.readValue(responseAfterPost, new TypeReference<>() {
        });
        Optional<EmployeePositionDto> positionDto = positionsAfterPost.stream().filter(p -> p.getPositionName().equals("Malarz")).findAny();
        assertTrue(positionDto.isEmpty());
        assertEquals(positionsBeforePost.size(), positionsAfterPost.size());
    }
}