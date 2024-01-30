package pl.kurs.finaltest.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.kurs.finaltest.FinalTestApplication;
import pl.kurs.finaltest.exceptions.ExceptionResponseDto;
import pl.kurs.finaltest.models.Student;
import pl.kurs.finaltest.models.commands.CreateEmployeePositionCommand;
import pl.kurs.finaltest.models.commands.CreateStudentCommand;
import pl.kurs.finaltest.models.commands.UpdateStudentCommand;
import pl.kurs.finaltest.models.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FinalTestApplication.class)
@AutoConfigureMockMvc
class PersonControllerIT {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void givenBodyWithStudent_whenPostRequestExecuted_thenOkIsReceived() throws Exception {
        //given
        String responseBefore = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", "99999999999"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<PersonDto> emptyList = objectMapper.readValue(responseBefore, new TypeReference<>() {
        });
        assertTrue(emptyList.isEmpty());

        Student student = new Student("test", "test", "99999999999", 0, 0, "test@test.pl", "test", 0, "test", 0);
        CreateStudentCommand createStudentCommand = modelMapper.map(student, CreateStudentCommand.class);
        createStudentCommand.setType("Student");
        String json = objectMapper.writeValueAsString(createStudentCommand);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        //then
        String responseAfter = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", "99999999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("test"))
                .andExpect(jsonPath("$[0].lastName").value("test"))
                .andExpect(jsonPath("$[0].pesel").value("99999999999"))
                .andExpect(jsonPath("$[0].height").value(0))
                .andExpect(jsonPath("$[0].weight").value(0))
                .andExpect(jsonPath("$[0].email").value("test@test.pl"))
                .andExpect(jsonPath("$[0].completedUniversity").value("test"))
                .andExpect(jsonPath("$[0].studyYear").value(0))
                .andExpect(jsonPath("$[0].fieldOfStudy").value("test"))
                .andExpect(jsonPath("$[0].scholarship").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<StudentDto> listWithStudent = objectMapper.readValue(responseAfter, new TypeReference<>() {
        });
        assertEquals(modelMapper.map(createStudentCommand, StudentDto.class), listWithStudent.get(0));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void givenBodyWithStudentWithWrongPeselAndEmail_whenPostRequestExecuted_thenBadRequestReceived() throws Exception {
        //given
        String responseBefore = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", "9999999999999"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<PersonDto> emptyList = objectMapper.readValue(responseBefore, new TypeReference<>() {
        });
        assertTrue(emptyList.isEmpty());

        Student student = new Student("test", "test", "9999999999999", 0, 0, "testtest.pl", "test", 0, "test", 0);
        CreateStudentCommand createStudentCommand = modelMapper.map(student, CreateStudentCommand.class);
        createStudentCommand.setType("Student");
        String json = objectMapper.writeValueAsString(createStudentCommand);

        //when
        String result = mvc.perform(MockMvcRequestBuilders.post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
        .andReturn()
        .getResponse()
        .getContentAsString();
        ExceptionResponseDto exceptionResponseDto = objectMapper.readValue(result, ExceptionResponseDto.class);

        //then
        String responseAfter = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", "99999999999"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<StudentDto> listWithStudent = objectMapper.readValue(responseAfter, new TypeReference<>() {
        });
        assertTrue(exceptionResponseDto.getErrorsMessages().contains("Field: pesel / rejected value: '9999999999999' / message: Wrong PESEL!"));
        assertTrue(exceptionResponseDto.getErrorsMessages().contains("Field: email / rejected value: 'testtest.pl' / message: must be a well-formed email address"));
        assertTrue(emptyList.isEmpty());
    }


    @Test
    void givenPostRequestWithoutAuthorization_then401ReceiveAndNoPostExecuted() throws Exception {
        //given
        Student student = new Student("test", "test", "99999999998", 0, 0, "test@test8.pl", "test", 0, "test", 0);
        CreateStudentCommand createStudentCommand = modelMapper.map(student, CreateStudentCommand.class);
        createStudentCommand.setType("Student");
        String json = objectMapper.writeValueAsString(createStudentCommand);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());

        //then
        String responseAfter = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", student.getPesel()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<StudentDto> listWithStudent = objectMapper.readValue(responseAfter, new TypeReference<>() {
        });
        assertTrue(listWithStudent.isEmpty());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void givenPostRequestWithNoTypeBody_then400ReceiveAndNoPostExecuted() throws Exception {
        //given
        Student student = new Student("test", "test", "99999999997", 0, 0, "test@test7.pl", "test", 0, "test", 0);
        CreateStudentCommand createStudentCommand = modelMapper.map(student, CreateStudentCommand.class);
        String json = objectMapper.writeValueAsString(createStudentCommand);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        //then
        String responseAfter = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", student.getPesel()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<StudentDto> listWithStudent = objectMapper.readValue(responseAfter, new TypeReference<>() {
        });
        assertTrue(listWithStudent.isEmpty());
    }

    @Test
    void givenGetByIdRequest_whenNoRetrievePerson_thenNotFoundReceive() throws Exception {
        //given
        mvc.perform(MockMvcRequestBuilders.delete("/api/persons/9999"));

        //then
        mvc.perform(MockMvcRequestBuilders.get("/api/persons/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.errorsMessages").value("Nie znaleziono osoby o id: 9999"));
    }

    @Test
    void givenGetByIdRequest_whenRetrievePerson_thenIsOk() throws Exception {
        //when
        //then
        mvc.perform(MockMvcRequestBuilders.get("/api/persons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Andrzej"))
                .andExpect(jsonPath("$.lastName").value("Andrzejewicz"))
                .andExpect(jsonPath("$.pesel").value("11111111111"))
                .andExpect(jsonPath("$.height").value(180))
                .andExpect(jsonPath("$.weight").value(100))
                .andExpect(jsonPath("$.email").value("test1@test.pl"))
                .andExpect(jsonPath("$.version").value(0));
    }

    @Test
    void givenGetRequestWithParameters_whenRetrievePerson_thenIsOk() throws Exception {
        //when
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "Employee");
        params.add("heightFrom", "175");
        params.add("heightTo", "180");
        params.add("weightFrom", "100");
        params.add("weightTo", "89");
        String response = mvc.perform(MockMvcRequestBuilders.get("/api/persons").params(params))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployeeDto> responseList = objectMapper.readValue(response, new TypeReference<>() {
        });

        //then
        List<EmployeeDto> filteredList = responseList.stream()
                .filter(employeeDto -> employeeDto.getHeight() >= 175 && employeeDto.getHeight() <= 180 &&
                        employeeDto.getWeight() >= 100 && employeeDto.getWeight() <= 89)
                .collect(Collectors.toList());
        assertEquals(responseList.size(), filteredList.size());

    }

    @WithMockUser(roles = "IMPORTER")
    @Test
    void givenPutRequestWithNoAuthentication_then403Receive() throws Exception {
        //Given
        String jsonBeforePut = mvc.perform(MockMvcRequestBuilders.get("/api/persons/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        StudentDto studentBeforeEdit = objectMapper.readValue(jsonBeforePut, StudentDto.class);


        //when
        studentBeforeEdit.setFirstName(studentBeforeEdit.getFirstName() + "test");
        UpdateStudentCommand editedStudent = modelMapper.map(studentBeforeEdit, UpdateStudentCommand.class);
        editedStudent.setVersion(0);
        editedStudent.setType("Student");
        String jsonToEdit = objectMapper.writeValueAsString(editedStudent);
        mvc.perform(MockMvcRequestBuilders.put("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToEdit))
                .andExpect(status().isForbidden());

        //then
        String getResult = mvc.perform(MockMvcRequestBuilders.get("/api/persons/" + studentBeforeEdit.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToEdit))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.readValue(getResult, StudentDto.class), studentBeforeEdit);
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void givenBodyWithStudent_whenPutRequestExecuted_thenIsOkReceive() throws Exception {
        //Given
        String jsonBeforePut = mvc.perform(MockMvcRequestBuilders.get("/api/persons/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        StudentDto studentBeforeEdit = objectMapper.readValue(jsonBeforePut, StudentDto.class);


        //when
        studentBeforeEdit.setFirstName(studentBeforeEdit.getFirstName() + "test");
        UpdateStudentCommand editedStudent = modelMapper.map(studentBeforeEdit, UpdateStudentCommand.class);
        editedStudent.setVersion(0);
        editedStudent.setType("Student");
        String jsonToEdit = objectMapper.writeValueAsString(editedStudent);
        mvc.perform(MockMvcRequestBuilders.put("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToEdit))
                .andExpect(status().isOk());

        //then
        String getResult = mvc.perform(MockMvcRequestBuilders.get("/api/persons/" + studentBeforeEdit.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToEdit))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.readValue(getResult, StudentDto.class), studentBeforeEdit);
    }

    @Test
    void givenGetPositionsRequest_whenRetrieveEmployeePositions_thenIsOk() throws Exception {
        //when
        //then
        String response = mvc.perform(MockMvcRequestBuilders.get("/api/persons/1/positions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].positionName").value("Architekt"))
                .andExpect(jsonPath("$[0].salary").value(5000))
                .andExpect(jsonPath("$[0].employmentStartDate[0]").value("2010"))
                .andExpect(jsonPath("$[0].employmentStartDate[1]").value("8"))
                .andExpect(jsonPath("$[0].employmentStartDate[2]").value("11"))
                .andExpect(jsonPath("$[0].employmentEndDate[0]").value("2014"))
                .andExpect(jsonPath("$[0].employmentEndDate[1]").value("1"))
                .andExpect(jsonPath("$[0].employmentEndDate[2]").value("31"))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void givenGetPositionsRequestWithWrongId_thenIsOk() throws Exception {
        //when
        //then
        String response = mvc.perform(MockMvcRequestBuilders.get("/api/persons/2/positions"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorsMessages").value("Podano zlego pracownika"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @WithMockUser(roles = "EMPLOYEE")
    @Test
    void givenPostPositionRequest_whenRequestExecuted_thenIsCreated() throws Exception {
        //given
        String responseBeforePost = mvc.perform(MockMvcRequestBuilders.get("/api/persons/1/positions"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployePositionDto> positionsBeforePost = objectMapper.readValue(responseBeforePost, new TypeReference<>() {
        });
        CreateEmployeePositionCommand command = new CreateEmployeePositionCommand(
                1L,
                "Projektant",
                2500,
                LocalDate.of(2009, 1, 1),
                LocalDate.of(2010, 7, 31)
        );
        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/persons/1/position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        String responseAfterPost = mvc.perform(MockMvcRequestBuilders.get("/api/persons/1/positions"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployePositionDto> positionsAfterPost = objectMapper.readValue(responseAfterPost, new TypeReference<>() {
        });
        EmployePositionDto positionDto = positionsAfterPost.stream().filter(p -> p.getPositionName().equals("Projektant")).findAny().orElseThrow();
        assertNotEquals(positionsBeforePost.size(), positionsAfterPost.size());
        assertEquals(positionDto.getEmployeeId(), command.getEmployeeId());
        assertEquals(positionDto.getPositionName(), command.getPositionName());
        assertEquals(positionDto.getSalary(), command.getSalary());
        assertEquals(positionDto.getEmploymentStartDate(), command.getEmploymentStartDate());
        assertEquals(positionDto.getEmploymentEndDate(), command.getEmploymentEndDate());
    }

    @WithMockUser(roles = "EMPLOYEE")
    @Test
    void givenPostWithWrongPositionRequest_whenRequestExecuted_thenBadRequest() throws Exception {
        //given
        String responseBeforePost = mvc.perform(MockMvcRequestBuilders.get("/api/persons/1/positions"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployePositionDto> positionsBeforePost = objectMapper.readValue(responseBeforePost, new TypeReference<>() {
        });
        CreateEmployeePositionCommand command = new CreateEmployeePositionCommand(
                1L,
                "Malarz",
                2500,
                LocalDate.of(2010, 8, 11),
                LocalDate.of(2013, 5, 12)
        );
        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/persons/1/position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        String responseAfterPost = mvc.perform(MockMvcRequestBuilders.get("/api/persons/1/positions"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<EmployePositionDto> positionsAfterPost = objectMapper.readValue(responseAfterPost, new TypeReference<>() {
        });
        Optional<EmployePositionDto> positionDto = positionsAfterPost.stream().filter(p -> p.getPositionName().equals("Malarz")).findAny();
        assertTrue(positionDto.isEmpty());
        assertEquals(positionsBeforePost.size(), positionsAfterPost.size());
    }

    @WithMockUser(roles = "IMPORTER")
    @Test
    void givenPostUploadRequestWithoutFile_ThenAccepted() throws Exception {
        //given
        String content = "Student,Imietest0,Nazwiskotest0,21311111111,0,0,test_st0@test.pl,test0,0,test0,0\n" +
                "Retiree,Imietest0,Nazwiskotest0,21311111311,0,0,test_re0@test.pl,0,0\n" +
                "Employee,Imietest0,Nazwiskotest0,21311111511,0,0,test_em0@test.pl,2000-01-01,test0,0";
        MockMultipartFile file = new MockMultipartFile("file", "persons-test-file.csv", MediaType.TEXT_PLAIN_VALUE, content.getBytes());
        String resultBeforeUpload = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("firstName", "Imietest0"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List listBeforeUpload = objectMapper.readValue(resultBeforeUpload, new TypeReference<>() {
        });
        assertEquals(0, listBeforeUpload.size());

        //when
        mvc.perform(MockMvcRequestBuilders.multipart("/api/persons/upload")
                .file(file))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status", Matchers.startsWith("Rozpoczeto import ")));
        mvc.perform(MockMvcRequestBuilders.get("/api/persons/upload"))
                .andExpect(status().isOk());
        Thread.sleep(100);

        //then
        String resultAfterUpload = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("firstName", "Imietest0"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List listAfterUpload = objectMapper.readValue(resultAfterUpload, new TypeReference<>() {
        });
        assertEquals(3, listAfterUpload.size());
    }

}