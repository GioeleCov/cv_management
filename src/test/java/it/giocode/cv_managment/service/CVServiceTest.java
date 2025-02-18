package it.giocode.cv_managment.service;

import it.giocode.cv_managment.dto.req.cv.CVReqDto;
import it.giocode.cv_managment.dto.req.cv.UpdateCVReqDto;
import it.giocode.cv_managment.dto.resp.cv.CVRespDto;
import it.giocode.cv_managment.entity.CVEntity;
import it.giocode.cv_managment.entity.CandidateEntity;
import it.giocode.cv_managment.exception.exception_class.AlreadyExistsException;
import it.giocode.cv_managment.exception.exception_class.NotFoundException;
import it.giocode.cv_managment.repository.CVRepository;
import it.giocode.cv_managment.repository.CandidateRepository;
import it.giocode.cv_managment.service.impl.CVServiceImpl;
import it.giocode.cv_managment.service.impl.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CVServiceTest {

    @Mock
    private CVRepository cvRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private CVServiceImpl cvService;

    private CVEntity cv;
    private CVReqDto cvReqDto;
    private CandidateEntity candidate;

    @BeforeEach
    void setUp() {
        candidate = CandidateEntity.builder()
                .candidateId(1L)
                .name("Name Test")
                .surname("Surname Test")
                .age(30)
                .phoneNumber("+39 340 XXXXXXX")
                .cvEntityList(new ArrayList<>())
                .build();

        cvReqDto = CVReqDto.builder()
                .cvTitle("Title Test")
                .education("Education Test")
                .spokenLanguage("Italiano, Inglese")
                .skills("Programmazione, Informatica")
                .experiences("Java, Spring, Docker")
                .fileName("File Test")
                .build();

        cv = CVEntity.builder()
                .cvId(1L)
                .cvTitle(cvReqDto.getCvTitle())
                .education(cvReqDto.getEducation())
                .spokenLanguage(cvReqDto.getSpokenLanguage())
                .skills(cvReqDto.getSkills())
                .experiences(cvReqDto.getExperiences())
                .fileName(cvReqDto.getFileName())
                .candidate(candidate)
                .build();
    }

    @Test
    void createCV_WhenCandidateWasFoundAndCVTitleDoesNotExist_ShouldReturnTrue() {
        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.of(candidate));

        boolean result = cvService.createCV(candidate.getCandidateId(), cvReqDto);

        assertTrue(result);
        assertEquals("Title Test", cv.getCvTitle());
        verify(candidateRepository).findById(candidate.getCandidateId());
    }

    @Test
    void createCV_WhenCandidateWasFoundAndCVTitleAlreadyExist_ShouldThrowAlreadyExistsException() {
        candidate.getCvEntityList().add(cv);

        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.of(candidate));

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> cvService.createCV(candidate.getCandidateId(), cvReqDto));

        assertEquals("CV 'File Test' already exists. Please insert a valid value", exception.getMessage());
        verify(candidateRepository).findById(candidate.getCandidateId());
    }

    @Test
    void createCV_WhenCandidateWasNotFound_ShouldThrowNotFoundException() {
        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> cvService.createCV(candidate.getCandidateId(), cvReqDto));

        assertEquals("Candidate not found with id '1'", exception.getMessage());
        verify(candidateRepository).findById(candidate.getCandidateId());
    }

    @Test
    void updateCV_WhenCVWasFoundAndUpdated_ShouldReturnTrue() {
        UpdateCVReqDto updatedCV = UpdateCVReqDto.builder()
                        .cvTitle("Updated Title test")
                        .build();

        when(cvRepository.findById(any(Long.class))).thenReturn(Optional.of(cv));

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.delete(any(Path.class))).thenAnswer(invocation -> null);

            boolean result = cvService.updateCV(cv.getCvId(), updatedCV);

            assertTrue(result);
            assertEquals(cv.getCvTitle(), updatedCV.getCvTitle());
            verify(cvRepository).findById(cv.getCvId());
        }
    }

    @Test
    void updateCV_WhenCVWasNotFound_ShouldThrowNotFoundException() {
        UpdateCVReqDto updateCVReqDto = UpdateCVReqDto.builder().build();

        when(cvRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> cvService.updateCV(cv.getCvId(), updateCVReqDto));

        assertEquals("CV not found with id '1'", exception.getMessage());
        verify(cvRepository).findById(cv.getCvId());
    }

    @Test
    void updateCV_WhenCVWasFoundAButNotUpdated_ShouldReturnFalse() {
        UpdateCVReqDto updatedCV = UpdateCVReqDto.builder()
                .cvTitle(null)
                .education(null)
                .spokenLanguage(null)
                .skills(null)
                .experiences(null)
                .profileImage(null)
                .fileName(null)
                .build();

        candidate.getCvEntityList().add(cv);

        when(cvRepository.findById(any(Long.class))).thenReturn(Optional.of(cv));

        boolean result = cvService.updateCV(cv.getCvId(), updatedCV);

        assertFalse(result);
        verify(cvRepository).findById(cv.getCvId());
    }

    @Test
    void deleteCV_WhenCandidateWasFoundAndCVWasFound_ShouldReturnTrue() {
        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.of(candidate));
        when(cvRepository.findById(any(Long.class))).thenReturn(Optional.of(cv));

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.delete(any(Path.class))).thenAnswer(invocation -> null);

            boolean result = cvService.deleteCV(candidate.getCandidateId(), cv.getCvId());

            assertTrue(result);
            verify(candidateRepository).findById(candidate.getCandidateId());
            verify(cvRepository).findById(cv.getCvId());
        }

    }

    @Test
    void deleteCV_WhenCandidateWasFoundButCVWasNotFound_ShouldThrowNotFoundException() {
        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.of(candidate));
        when(cvRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> cvService.deleteCV(candidate.getCandidateId(), cv.getCvId()));

        assertEquals("CV not found with id '1'", exception.getMessage());
        verify(candidateRepository).findById(candidate.getCandidateId());
        verify(cvRepository).findById(cv.getCvId());
    }

    @Test
    void deleteCV_WhenCandidateWasNotFound_ShouldThrowNotFoundException() {
        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> cvService.deleteCV(candidate.getCandidateId(), cv.getCvId()));

        assertEquals("Candidate not found with id '1'", exception.getMessage());
        verify(candidateRepository).findById(candidate.getCandidateId());
    }

    @Test
    void findAll_WhenSearchAllCVs_ShouldReturnACVList() {
        List<CVEntity> cvEntityList = List.of(cv);

        when(cvRepository.findAll()).thenReturn(cvEntityList);

        List<CVRespDto> cvRespDtoList = cvService.findAll();

        assertFalse(cvRespDtoList.isEmpty());
        assertEquals(cv.getCvTitle(), cvRespDtoList.get(0).getCvTitle());
        verify(cvRepository).findAll();
    }

    @Test
    void findAll_WhenSearchAllCVsButFindNothing_ShouldReturnAnEmptyList() {
        when(cvRepository.findAll()).thenReturn(Collections.emptyList());

        List<CVRespDto> cvRespDtoList = cvService.findAll();

        assertTrue(cvRespDtoList.isEmpty());
        verify(cvRepository).findAll();
    }
}
