package it.giocode.cv_managment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.giocode.cv_managment.dto.req.candidate.CandidateReqDto;
import it.giocode.cv_managment.dto.req.candidate.UpdateCandidateReqDto;
import it.giocode.cv_managment.dto.resp.candidate.CandidateRespDto;
import it.giocode.cv_managment.dto.resp.cv.CVRespDto;
import it.giocode.cv_managment.entity.UserEntity;
import it.giocode.cv_managment.repository.UserRepository;
import it.giocode.cv_managment.config.TestSecurityConfig;
import it.giocode.cv_managment.service.iface.ICandidateService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CandidateController.class)
@Import(TestSecurityConfig.class)
public class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICandidateService candidateService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private CandidateController candidateController;

    private static CandidateReqDto candidateReqDto;
    private static List<CandidateRespDto> candidateRespDtoList;
    private static CandidateRespDto candidateRespDto;

    @BeforeAll
    static void setUp() {
        candidateReqDto = CandidateReqDto.builder()
                .name("Name Test")
                .surname("Surname Test")
                .age(30)
                .phoneNumber("+39 340 0539742")
                .build();

        List<CVRespDto> cvRespDtoList = List.of(
                CVRespDto.builder()
                        .cvTitle("Title Test")
                        .experiences("Java, Spring, Docker")
                        .skills("Informatica, Programmazione, Excel")
                        .profileImage("Image Test")
                        .fileName("File Test")
                        .build()
        );

        candidateRespDto = CandidateRespDto.builder()
                .name(candidateReqDto.getName())
                .surname(candidateReqDto.getSurname())
                .age(candidateReqDto.getAge())
                .phoneNumber(candidateReqDto.getPhoneNumber())
                .cvRespDtoList(cvRespDtoList)
                .build();
    }

    @Test
    public void createCandidate_WhenServiceProcessTheCreationRequest_ShouldReturnStatus201() throws Exception {
        when(candidateService.createCandidate(any(Long.class), any(CandidateReqDto.class))).thenReturn(true);
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(UserEntity.builder().userId(1L).build()));

        String requestJson = objectMapper.writeValueAsString(candidateReqDto);

        mockMvc.perform(post("/api/candidate/create/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("Candidate created successfully"))
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    public void createCandidate_WhenServiceProcessFailTheCreationRequest_ShouldReturnStatus500() throws Exception {
        when(candidateService.createCandidate(any(Long.class), any(CandidateReqDto.class))).thenReturn(false);

        String requestJson = objectMapper.writeValueAsString(candidateReqDto);

        mockMvc.perform(post("/api/candidate/create/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Something went wrong. Please try later"));
    }

    @Test
    public void updateCandidate_WhenServiceProcessTheUpdateRequest_ShouldReturnStatus200() throws Exception {
        when(candidateService.updateCandidate(any(Long.class), any(UpdateCandidateReqDto.class))).thenReturn(true);

        String requestJson = objectMapper.writeValueAsString(candidateReqDto);

        mockMvc.perform(put("/api/candidate/update/{candidateId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Candidate updated successfully"));
    }

    @Test
    public void updateCandidate_WhenServiceProcessFailTheUpdateRequest_ShouldReturnStatus500() throws Exception {
        when(candidateService.updateCandidate(any(Long.class), any(UpdateCandidateReqDto.class))).thenReturn(false);

        String requestJson = objectMapper.writeValueAsString(candidateReqDto);

        mockMvc.perform(put("/api/candidate/update/{candidateId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Something went wrong. Please try later"));
    }

    @Test
    public void deleteCandidate_WhenServiceProcessTheDeleteRequest_ShouldReturnStatus200() throws Exception {
        when(candidateService.deleteCandidate(any(Long.class))).thenReturn(true);

        String requestJson = objectMapper.writeValueAsString(candidateReqDto);

        mockMvc.perform(delete("/api/candidate/delete/{candidateId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Candidate deleted successfully"));
    }

    @Test
    public void deleteCandidate_WhenServiceProcessFailTheDeleteRequest_ShouldReturnStatus500() throws Exception {
        when(candidateService.deleteCandidate(any(Long.class))).thenReturn(false);

        String requestJson = objectMapper.writeValueAsString(candidateReqDto);

        mockMvc.perform(delete("/api/candidate/delete/{candidateId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Something went wrong. Please try later"));
    }

    @Test void getCandidatesByName_WhenServiceProcessTheFindByNameRequest_ShouldReturnStatus200() throws Exception {
        candidateRespDtoList = List.of(candidateRespDto);

        when(candidateService.findByName(any(String.class))).thenReturn(candidateRespDtoList);


        mockMvc.perform(get("/api/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "Name Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Name Test"));
    }

    @Test void getCandidatesByName_WhenServiceProcessFailTheFindByNameRequest_ShouldReturnStatus404() throws Exception {

        when(candidateService.findByName(any(String.class))).thenReturn(Collections.emptyList());


        mockMvc.perform(get("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "Name Test"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test void findAll_WhenServiceProcessTheFindAllRequest_ShouldReturnStatus200() throws Exception {
        candidateRespDtoList = List.of(candidateRespDto);

        when(candidateService.findAll()).thenReturn(candidateRespDtoList);


        mockMvc.perform(get("/api/candidates/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Name Test"));
    }

    @Test void findAll_WhenServiceProcessFailTheFindAllRequest_ShouldReturnStatus404() throws Exception {

        when(candidateService.findAll()).thenReturn(Collections.emptyList());


        mockMvc.perform(get("/api/candidates/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test void getCandidatesByExperiences_WhenServiceProcessTheFindAllRequest_ShouldReturnStatus200() throws Exception {
        candidateRespDtoList = List.of(candidateRespDto);

        when(candidateService.findByExperiences("Java")).thenReturn(candidateRespDtoList);


        mockMvc.perform(get("/api/candidates/experiences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("experiences", "Java"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Name Test"));
    }

    @Test void getCandidatesByExperiences_WhenServiceProcessFailTheFindAllRequest_ShouldReturnStatus404() throws Exception {

        when(candidateService.findByExperiences("Java")).thenReturn(Collections.emptyList());


        mockMvc.perform(get("/api/candidates/experiences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("experiences", "Java"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test void getCandidatesBySkills_WhenServiceProcessTheFindAllRequest_ShouldReturnStatus200() throws Exception {
        candidateRespDtoList = List.of(candidateRespDto);

        when(candidateService.findBySkills("Programmazione")).thenReturn(candidateRespDtoList);


        mockMvc.perform(get("/api/candidates/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("skills", "Programmazione"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Name Test"));
    }

    @Test void getCandidatesBySkills_WhenServiceProcessFailTheFindAllRequest_ShouldReturnStatus404() throws Exception {

        when(candidateService.findBySkills("Programmazione")).thenReturn(Collections.emptyList());


        mockMvc.perform(get("/api/candidates/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("skills", "Programmazione"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}
