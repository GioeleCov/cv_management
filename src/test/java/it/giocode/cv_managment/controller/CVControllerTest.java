package it.giocode.cv_managment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.giocode.cv_managment.dto.req.cv.CVReqDto;
import it.giocode.cv_managment.dto.req.cv.UpdateCVReqDto;
import it.giocode.cv_managment.dto.resp.cv.CVRespDto;
import it.giocode.cv_managment.config.TestSecurityConfig;
import it.giocode.cv_managment.service.iface.ICVService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CVController.class)
@Import(TestSecurityConfig.class)
public class CVControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ICVService cvService;

    @InjectMocks
    private CVController cvController;

    private static CVReqDto cvReqDto;


    @BeforeAll
    static void setUp() {

        cvReqDto = CVReqDto.builder()
                .cvTitle("Title Test")
                .education("Education Test")
                .spokenLanguage("Italiano, Inglese")
                .skills("Programmazione, Informatica")
                .experiences("Java, Spring, Docker")
                .fileName("File Test")
                .build();
    }

    @Test
    public void createCV_WhenServiceProcessTheCreationRequest_ShouldReturnStatus201() throws Exception {
        when(cvService.createCV(any(Long.class), any(CVReqDto.class))).thenReturn(true);

        String requestJson = objectMapper.writeValueAsString(cvReqDto);

        mockMvc.perform(post("/api/cv/create/{candidateId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("CV created successfully"));
    }

    @Test
    public void createCV_WhenServiceProcessFailTheCreationRequest_ShouldReturnStatus500() throws Exception {
        when(cvService.createCV(any(Long.class), any(CVReqDto.class))).thenReturn(false);

        String requestJson = objectMapper.writeValueAsString(cvReqDto);

        mockMvc.perform(post("/api/cv/create/{candidateId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Something went wrong. Please try later"));
    }

    @Test
    public void updateCV_WhenServiceProcessTheUpdateRequest_ShouldReturnStatus200() throws Exception {
        when(cvService.updateCV(any(Long.class), any(UpdateCVReqDto.class))).thenReturn(true);

        String requestJson = objectMapper.writeValueAsString(cvReqDto);

        mockMvc.perform(put("/api/cv/update/{cvId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("CV updated successfully"));
    }

    @Test
    public void updateCV_WhenServiceProcessFailTheUpdateRequest_ShouldReturnStatus500() throws Exception {
        when(cvService.updateCV(any(Long.class), any(UpdateCVReqDto.class))).thenReturn(false);

        String requestJson = objectMapper.writeValueAsString(cvReqDto);

        mockMvc.perform(put("/api/cv/update/{cvId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Something went wrong. Please try later"));
    }

    @Test
    public void deleteCV_WhenServiceProcessTheDeleteRequest_ShouldReturnStatus200() throws Exception {
        when(cvService.deleteCV(any(Long.class), any(Long.class))).thenReturn(true);

        mockMvc.perform(delete("/api/cv/delete/{candidateId}/{cvId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("CV deleted successfully"));
    }

    @Test
    public void deleteCV_WhenServiceProcessFailTheDeleteRequest_ShouldReturnStatus200() throws Exception {
        when(cvService.deleteCV(any(Long.class), any(Long.class))).thenReturn(false);

        mockMvc.perform(delete("/api/cv/delete/{candidateId}/{cvId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Something went wrong. Please try later"));
    }

    @Test void findAll_WhenServiceProcessTheFindAllRequest_ShouldReturnStatus200() throws Exception {
        CVRespDto cvRespDto = CVRespDto.builder()
                .cvTitle("Title Test")
                .build();

        List<CVRespDto> cvRespDtoList = List.of(cvRespDto);

        when(cvService.findAll()).thenReturn(cvRespDtoList);

        mockMvc.perform(get("/api/cv/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].cvTitle").value("Title Test"));
    }

    @Test void findAll_WhenServiceProcessFailTheFindAllRequest_ShouldReturnStatus404() throws Exception {
        when(cvService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/cv/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}
