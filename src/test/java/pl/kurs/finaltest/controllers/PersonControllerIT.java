package pl.kurs.finaltest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import pl.kurs.finaltest.models.commands.PersonCommand;
import pl.kurs.finaltest.models.dto.EmployeeDto;
import pl.kurs.finaltest.models.dto.PersonDto;
import pl.kurs.finaltest.models.dto.StudentDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FinalTestApplication.class)
@AutoConfigureMockMvc
class PersonControllerIT {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    private PersonCommand studentCommandForTests = new PersonCommand();
    private String jsonWithStudentCommand;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());

        studentCommandForTests.setName("Student");
        Map<String, Object> studentParameters = new HashMap<>();
        studentParameters.put("firstName", "test");
        studentParameters.put("lastName", "test");
        studentParameters.put("pesel", "99999999999");
        studentParameters.put("height", 0.0);
        studentParameters.put("weight", 0.0);
        studentParameters.put("email", "test@test.pl");
        studentParameters.put("completedUniversity", "test");
        studentParameters.put("studyYear", 0);
        studentParameters.put("fieldOfStudy", "test");
        studentParameters.put("scholarship", 0.0);
        studentCommandForTests.setParameters(studentParameters);
        jsonWithStudentCommand = objectMapper.writeValueAsString(studentCommandForTests);
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
        JsonNode jsonNode = objectMapper.readTree(responseBefore);
        String content = jsonNode.get("content").toString();
        List<StudentDto> listBeforePost = objectMapper.readValue(content, new TypeReference<>() {
        });

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithStudentCommand))
                .andExpect(status().isCreated());

        //then
        String responseAfter = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", "99999999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].firstName").value("test"))
                .andExpect(jsonPath("content[0].lastName").value("test"))
                .andExpect(jsonPath("content[0].pesel").value("99999999999"))
                .andExpect(jsonPath("content[0].height").value(0))
                .andExpect(jsonPath("content[0].weight").value(0))
                .andExpect(jsonPath("content[0].email").value("test@test.pl"))
                .andExpect(jsonPath("content[0].completedUniversity").value("test"))
                .andExpect(jsonPath("content[0].studyYear").value(0))
                .andExpect(jsonPath("content[0].fieldOfStudy").value("test"))
                .andExpect(jsonPath("content[0].scholarship").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode jsonNodeAfter = objectMapper.readTree(responseAfter);
        String contentAfter = jsonNodeAfter.get("content").toString();
        List<StudentDto> listAfterPost = objectMapper.readValue(contentAfter, new TypeReference<>() {
        });
        assertNotEquals(listBeforePost.size(), listAfterPost.size());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void givenBodyWithStudentWithWrongPeselAndEmail_whenPostRequestExecuted_thenBadRequestReceived() throws Exception {
        //given
        String responseBefore = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", "99999999999"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBefore);
        String content = jsonNode.get("content").toString();
        List<StudentDto> listBeforePost = objectMapper.readValue(content, new TypeReference<>() {
        });

        studentCommandForTests.getParameters().replace("pesel", "999999999999911");
        studentCommandForTests.getParameters().replace("email", "111");
        String json = objectMapper.writeValueAsString(studentCommandForTests);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        String responseAfter = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", "99999999999"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode jsonNodeAfterPost = objectMapper.readTree(responseAfter);
        String contentAfterPost = jsonNodeAfterPost.get("content").toString();
        List<StudentDto> listAfterPost = objectMapper.readValue(contentAfterPost, new TypeReference<>() {
        });
        assertEquals(listBeforePost.size(), listAfterPost.size());
    }


    @Test
    void givenPostRequestWithoutAuthorization_then401ReceiveAndNoPostExecuted() throws Exception {
        //given
        studentCommandForTests.getParameters().replace("pesel", "99999999998");
        String json = objectMapper.writeValueAsString(studentCommandForTests);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());

        //then
        String responseAfter = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", "99999999998"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode jsonNodeAfterPost = objectMapper.readTree(responseAfter);
        String contentAfterPost = jsonNodeAfterPost.get("content").toString();
        List<StudentDto> listWithStudent = objectMapper.readValue(contentAfterPost, new TypeReference<>() {
        });
        assertTrue(listWithStudent.isEmpty());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void givenPostRequestWithNoTypeBody_then400ReceiveAndNoPostExecuted() throws Exception {
        //given
        studentCommandForTests.setName(null);
        studentCommandForTests.getParameters().replace("pesel", "99999999997");
        String json = objectMapper.writeValueAsString(studentCommandForTests);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        //then
        String responseAfter = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("pesel", "99999999997"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode jsonNodeAfterPost = objectMapper.readTree(responseAfter);
        String contentAfterPost = jsonNodeAfterPost.get("content").toString();
        List<StudentDto> listWithStudent = objectMapper.readValue(contentAfterPost, new TypeReference<>() {
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
        JsonNode jsonNode = objectMapper.readTree(response);
        String content = jsonNode.get("content").toString();
        List<EmployeeDto> responseList = objectMapper.readValue(content, new TypeReference<>() {
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
        String jsonBeforePut = mvc.perform(MockMvcRequestBuilders.get("/api/persons/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        StudentDto studentBeforeEdit = objectMapper.readValue(jsonBeforePut, StudentDto.class);

        //when
        mvc.perform(MockMvcRequestBuilders.put("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithStudentCommand))
                .andExpect(status().isForbidden());

        //then
        String result = mvc.perform(MockMvcRequestBuilders.get("/api/persons/" + studentBeforeEdit.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.readValue(result, StudentDto.class), studentBeforeEdit);
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
        studentCommandForTests.getParameters().replace("firstName", studentBeforeEdit.getFirstName() + "_edit");
        studentCommandForTests.getParameters().replace("lastName", studentBeforeEdit.getLastName());
        studentCommandForTests.getParameters().replace("pesel", studentBeforeEdit.getPesel());
        studentCommandForTests.getParameters().replace("email", studentBeforeEdit.getEmail());
        studentCommandForTests.getParameters().replace("height", studentBeforeEdit.getHeight());
        studentCommandForTests.getParameters().replace("weight", studentBeforeEdit.getWeight());
        studentCommandForTests.getParameters().replace("completedUniversity", studentBeforeEdit.getCompletedUniversity());
        studentCommandForTests.getParameters().replace("studyYear", studentBeforeEdit.getStudyYear());
        studentCommandForTests.getParameters().replace("fieldOfStudy", studentBeforeEdit.getFieldOfStudy());
        studentCommandForTests.getParameters().replace("scholarship", studentBeforeEdit.getScholarship());
        studentCommandForTests.getParameters().put("id", studentBeforeEdit.getId());
        studentCommandForTests.getParameters().put("version", studentBeforeEdit.getVersion());
        jsonWithStudentCommand = objectMapper.writeValueAsString(studentCommandForTests);

        mvc.perform(MockMvcRequestBuilders.put("/api/persons/" + studentBeforeEdit.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithStudentCommand))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        String result = mvc.perform(MockMvcRequestBuilders.get("/api/persons/" + studentBeforeEdit.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        StudentDto studentAfterEdit = objectMapper.readValue(result, StudentDto.class);
        assertNotEquals(studentAfterEdit, studentBeforeEdit);
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void givenBodyWithStudentWithNoChanges_whenPutRequestExecuted_thenIsBadRequest() throws Exception {
        //Given
        String jsonBeforePut = mvc.perform(MockMvcRequestBuilders.get("/api/persons/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        StudentDto studentBeforeEdit = objectMapper.readValue(jsonBeforePut, StudentDto.class);


        //when
        studentCommandForTests.getParameters().replace("firstName", studentBeforeEdit.getFirstName());
        studentCommandForTests.getParameters().replace("lastName", studentBeforeEdit.getLastName());
        studentCommandForTests.getParameters().replace("pesel", studentBeforeEdit.getPesel());
        studentCommandForTests.getParameters().replace("email", studentBeforeEdit.getEmail());
        studentCommandForTests.getParameters().replace("height", studentBeforeEdit.getHeight());
        studentCommandForTests.getParameters().replace("weight", studentBeforeEdit.getWeight());
        studentCommandForTests.getParameters().replace("completedUniversity", studentBeforeEdit.getCompletedUniversity());
        studentCommandForTests.getParameters().replace("studyYear", studentBeforeEdit.getStudyYear());
        studentCommandForTests.getParameters().replace("fieldOfStudy", studentBeforeEdit.getFieldOfStudy());
        studentCommandForTests.getParameters().replace("scholarship", studentBeforeEdit.getScholarship());
        studentCommandForTests.getParameters().put("id", studentBeforeEdit.getId());
        studentCommandForTests.getParameters().put("version", studentBeforeEdit.getVersion());
        jsonWithStudentCommand = objectMapper.writeValueAsString(studentCommandForTests);

        mvc.perform(MockMvcRequestBuilders.put("/api/persons/" + studentBeforeEdit.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithStudentCommand))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        String result = mvc.perform(MockMvcRequestBuilders.get("/api/persons/" + studentBeforeEdit.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        StudentDto studentAfterEdit = objectMapper.readValue(result, StudentDto.class);
        assertEquals(studentAfterEdit, studentBeforeEdit);
    }

    @WithMockUser(roles = "IMPORTER")
    @Test
    void givenPostUploadRequest_ThenAccepted() throws Exception {
        //given
        String contentToImport = "Student,Imietest0,Nazwiskotest0,21311111111,0,0,test_st0@test.pl,test0,0,test0,0\n" +
                "Retiree,Imietest0,Nazwiskotest0,21311111311,0,0,test_re0@test.pl,0,0\n" +
                "Employee,Imietest0,Nazwiskotest0,21311111511,0,0,test_em0@test.pl,2000-01-01,test0,0";
        MockMultipartFile file = new MockMultipartFile("file", "persons-test-file.csv", MediaType.TEXT_PLAIN_VALUE, contentToImport.getBytes());
        String resultBeforeImport = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("firstName", "Imietest0"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode jsonNodeBeforeImport = objectMapper.readTree(resultBeforeImport);
        String contentBeforeImport = jsonNodeBeforeImport.get("content").toString();
        List<PersonDto> emptyList = objectMapper.readValue(contentBeforeImport, new TypeReference<>() {
        });
        assertTrue(emptyList.isEmpty());

        //when
        String importResponse = mvc.perform(MockMvcRequestBuilders.multipart("/api/persons/import")
                .file(file))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status", Matchers.startsWith("Rozpoczeto import ")))
                .andReturn()
                .getResponse()
                .getContentAsString();
        int importStatusId = Integer.parseInt(importResponse.substring(importResponse.lastIndexOf(' ') + 1, importResponse.lastIndexOf('"')));
        mvc.perform(MockMvcRequestBuilders.get("/api/persons/import-status/" + importStatusId))
                .andExpect(status().isOk());
        Thread.sleep(100);

        //then
        String resultAfterImport = mvc.perform(MockMvcRequestBuilders.get("/api/persons").param("firstName", "Imietest0"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode jsonNodeAfterImport = objectMapper.readTree(resultAfterImport);
        String contentAfterImport = jsonNodeAfterImport.get("content").toString();
        List listWithPersons = objectMapper.readValue(contentAfterImport, new TypeReference<>() {
        });
        assertEquals(3, listWithPersons.size());
    }

}