package it.giocode.cv_managment.service;

import it.giocode.cv_managment.dto.req.candidate.CandidateReqDto;
import it.giocode.cv_managment.dto.req.candidate.UpdateCandidateReqDto;
import it.giocode.cv_managment.dto.resp.candidate.CandidateRespDto;
import it.giocode.cv_managment.entity.CVEntity;
import it.giocode.cv_managment.entity.CandidateEntity;
import it.giocode.cv_managment.entity.UserEntity;
import it.giocode.cv_managment.exception.exception_class.AlreadyExistsException;
import it.giocode.cv_managment.exception.exception_class.NotFoundException;
import it.giocode.cv_managment.repository.CandidateRepository;
import it.giocode.cv_managment.repository.UserRepository;
import it.giocode.cv_managment.service.impl.CandidateServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CandidateServiceImpl candidateService;

    private static UserEntity user;
    private static CandidateEntity candidate;
    private static CandidateReqDto candidateReqDto;

    @BeforeEach
    void setUpEach() {
        List<CVEntity> cvEntityList = List.of(
                CVEntity.builder()
                        .cvTitle("Title Test")
                        .experiences("Java, Spring, Docker")
                        .skills("Informatica, Programmazione, Excel")
                        .profileImage("Image Test")
                        .fileName("File Test")
                        .build()
        );

        candidate = CandidateEntity.builder()
                .candidateId(1L)
                .name(candidateReqDto.getName())
                .surname(candidateReqDto.getSurname())
                .age(candidateReqDto.getAge())
                .phoneNumber(candidateReqDto.getPhoneNumber())
                .user(UserEntity.builder().userId(1L).build())
                .cvEntityList(cvEntityList)
                .build();
    }

    @BeforeAll
    static void setUp() {
        user = UserEntity.builder()
                .userId(1L)
                .email("test@example.com")
                .password("testPassword")
                .build();

        candidateReqDto = CandidateReqDto.builder()
                .name("Name Test")
                .surname("Surname Test")
                .age(30)
                .phoneNumber("+39 340 XXXXXXX")
                .build();
    }

    @Test
    void createCandidate_WhenCandidateDoesNotExist_ShouldReturnTrue() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(candidateRepository.existsByPhoneNumber(any(String.class))).thenReturn(false);

        boolean result = candidateService.createCandidate(user.getUserId(), candidateReqDto);

        assertTrue(result);
        verify(userRepository).findById(user.getUserId());
        verify(candidateRepository).existsByPhoneNumber(candidate.getPhoneNumber());
    }

    @Test
    void createCandidate_WhenCandidateAlreadyExist_ShouldThrowAlreadyExistsException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(candidateRepository.existsByPhoneNumber(any(String.class))).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> candidateService.createCandidate(user.getUserId(), candidateReqDto));

        assertEquals( "Phone Number '+39 340 XXXXXXX' already exists. Please insert a valid value",
                exception.getMessage());
    }

    @Test
    void createCandidate_WhenUserNotFound_ShouldThrowNotFoundException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> candidateService.createCandidate(user.getUserId(), candidateReqDto));

        assertEquals( "User not found with id '1'",
                exception.getMessage());
    }

    @Test
    void updateCandidate_WhenCandidateWasFoundAndUpdated_ShouldReturnTrue() {
        UpdateCandidateReqDto updateCandidateReq = UpdateCandidateReqDto.builder()
                .name("Updated Name Test")
                .build();

        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.of(candidate));

        boolean result = candidateService.updateCandidate(candidate.getCandidateId(), updateCandidateReq);

        assertTrue(result);
        assertEquals("Updated Name Test", candidate.getName());
        verify(candidateRepository).findById(candidate.getCandidateId());
    }

    @Test
    void updateCandidate_WhenCandidateWasFoundButNotUpdated_ShouldReturnFalse() {
        UpdateCandidateReqDto updateCandidateReq = UpdateCandidateReqDto.builder()
                .name("Name Test")
                .build();

        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(candidate));

        boolean result = candidateService.updateCandidate(candidate.getCandidateId(), updateCandidateReq);

        assertFalse(result);
        assertEquals("Name Test", candidate.getName());
        verify(candidateRepository).findById(candidate.getCandidateId());
    }

    @Test
    void updateCandidate_WhenCandidateWasFoundButTheFieldsToUpdateAreNull_ShouldReturnFalse() {
        UpdateCandidateReqDto updateCandidateReq = UpdateCandidateReqDto.builder()
                .name(null)
                .surname(null)
                .age(null)
                .phoneNumber(null)
                .build();

        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.of(candidate));

        boolean result = candidateService.updateCandidate(candidate.getCandidateId(), updateCandidateReq);

        assertFalse(result);
        assertEquals("Name Test", candidate.getName());
        verify(candidateRepository).findById(candidate.getCandidateId());
    }

    @Test
    void updateCandidate_WhenCandidateNotFound_ShouldThrowNotFoundException() {
        UpdateCandidateReqDto updateCandidateReq = UpdateCandidateReqDto.builder().build();

        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> candidateService.updateCandidate(candidate.getCandidateId(), updateCandidateReq));

        assertEquals( "Candidate not found with id '1'",
                exception.getMessage());
    }

    @Test
    void deleteCandidate_WhenCandidateWasFoundAndDeleted_ShouldReturnTrue() {
        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.of(candidate));

        boolean result = candidateService.deleteCandidate(candidate.getCandidateId());

        assertTrue(result);
        verify(candidateRepository).findById(candidate.getCandidateId());
    }

    @Test
    void deleteCandidate_WhenCandidateNotFound_ShouldThrowNotFoundException() {
        when(candidateRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> candidateService.deleteCandidate(candidate.getCandidateId()));

        assertEquals( "Candidate not found with id '1'",
                exception.getMessage());
    }

    @Test
    void findByName_WhenSearchForACandidateByName_ShouldReturnACandidateList() {
        List<CandidateEntity> candidateEntityList = List.of(candidate);

        when(candidateRepository.findByName(any(String.class))).thenReturn(candidateEntityList);

        List<CandidateRespDto> result = candidateService.findByName(candidate.getName());

        assertFalse(result.isEmpty());
        assertEquals(candidate.getName(), result.get(0).getName());
        verify(candidateRepository).findByName(candidate.getName());
    }

    @Test
    void findByName_WhenSearchForACandidateByNameButFindNothing_ShouldReturnAnEmptyList() {
        when(candidateRepository.findByName(any(String.class))).thenReturn(Collections.emptyList());

        List<CandidateRespDto> result = candidateService.findByName(candidate.getName());

        assertTrue(result.isEmpty());
        verify(candidateRepository).findByName(candidate.getName());
    }

    @Test
    void findAll_WhenSearchAllCandidates_ShouldReturnACandidateList() {
        List<CandidateEntity> candidateEntityList = List.of(candidate);

        when(candidateRepository.findAll()).thenReturn(candidateEntityList);

        List<CandidateRespDto> result = candidateService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(candidate.getName(), result.get(0).getName());
        verify(candidateRepository).findAll();
    }

    @Test
    void findAll_WhenSearchAllCandidatesButFindNothing_ShouldReturnAnEmptyList() {
        when(candidateRepository.findAll()).thenReturn(Collections.emptyList());

        List<CandidateRespDto> result = candidateService.findAll();

        assertTrue(result.isEmpty());
        verify(candidateRepository).findAll();
    }

    @Test
    void findByExperiences_WhenForSearchACandidateByExperiences_ShouldReturnACandidateList() {
        List<CandidateEntity> candidateEntityList = List.of(candidate);

        when(candidateRepository.findAll()).thenReturn(candidateEntityList);

        List<CandidateRespDto> result = candidateService.findByExperiences("Java");

        assertFalse(result.isEmpty());
        assertEquals(candidate.getName(), result.get(0).getName());
        verify(candidateRepository).findAll();
    }

    @Test
    void findByExperiences_WhenSearchForACandidateByExperiencesButFindNothing_ShouldReturnAnEmptyList() {

        when(candidateRepository.findAll()).thenReturn(Collections.emptyList());

        List<CandidateRespDto> result = candidateService.findByExperiences("Java");

        assertTrue(result.isEmpty());
        verify(candidateRepository).findAll();
    }

    @Test
    void findBySkills_WhenSearchForACandidateBySkills_ShouldReturnACandidateList() {
        List<CandidateEntity> candidateEntityList = List.of(candidate);

        when(candidateRepository.findAll()).thenReturn(candidateEntityList);

        List<CandidateRespDto> result = candidateService.findBySkills("Programmazione");

        assertFalse(result.isEmpty());
        assertEquals(candidate.getName(), result.get(0).getName());
        verify(candidateRepository).findAll();
    }

    @Test
    void findBySkills_WhenSearchForACandidateBySkillsButFindNothing_ShouldReturnAnEmptyList() {

        when(candidateRepository.findAll()).thenReturn(Collections.emptyList());

        List<CandidateRespDto> result = candidateService.findBySkills("Programmazione");

        assertTrue(result.isEmpty());
        verify(candidateRepository).findAll();
    }

}
