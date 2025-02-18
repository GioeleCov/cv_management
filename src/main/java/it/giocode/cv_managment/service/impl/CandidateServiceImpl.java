package it.giocode.cv_managment.service.impl;

import it.giocode.cv_managment.dto.req.candidate.CandidateReqDto;
import it.giocode.cv_managment.dto.req.candidate.UpdateCandidateReqDto;
import it.giocode.cv_managment.dto.resp.candidate.CandidateRespDto;
import it.giocode.cv_managment.entity.CandidateEntity;
import it.giocode.cv_managment.entity.UserEntity;
import it.giocode.cv_managment.exception.exception_class.AlreadyExistsException;
import it.giocode.cv_managment.exception.exception_class.NotFoundException;
import it.giocode.cv_managment.mapper.CandidateMapper;
import it.giocode.cv_managment.repository.CandidateRepository;
import it.giocode.cv_managment.repository.UserRepository;
import it.giocode.cv_managment.service.iface.ICandidateService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CandidateServiceImpl implements ICandidateService {

    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;

    @Override
    public boolean createCandidate(Long userId, CandidateReqDto candidateReqDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", "id", userId.toString()));

        if (candidateRepository.existsByPhoneNumber(candidateReqDto.getPhoneNumber())) {
            throw new AlreadyExistsException("Phone Number", candidateReqDto.getPhoneNumber());
        }

        CandidateEntity candidate = CandidateMapper.mapToCandidateEntity(candidateReqDto);
        candidate.setUser(user);
        candidate.setCreatedAt(LocalDateTime.now());
        candidateRepository.save(candidate);
        return true;
    }

    @Override
    public boolean updateCandidate(Long candidateId, UpdateCandidateReqDto updateCandidateReqDto) {
        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate", "id", candidateId.toString()));

        CandidateReqDto candidateReqDto = CandidateMapper.mapReqDtoToUpdateReqDto(updateCandidateReqDto);

        if (checkIfItNeedsUpdating(candidateReqDto, candidate)) {
            candidate.setUpdatedAt(LocalDateTime.now());
            candidateRepository.save(candidate);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteCandidate(Long candidateId) {
        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate", "id", candidateId.toString()));

        candidateRepository.delete(candidate);
        return true;
    }

    @Override
    public List<CandidateRespDto> findByName(String name) {
        List<CandidateEntity> candidateEntityList = candidateRepository.findByName(name);

        if (candidateEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        return candidateEntityList.stream()
                .map(CandidateMapper::mapToCandidateRespDto)
                .toList();
    }

    @Override
    public List<CandidateRespDto> findAll() {
        List<CandidateEntity> candidateEntityList = candidateRepository.findAll();

        if (candidateEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        return candidateEntityList.stream()
                .map(CandidateMapper::mapToCandidateRespDto)
                .toList();
    }

    @Override
    public List<CandidateRespDto> findByExperiences(String experiences) {
        List<CandidateEntity> candidateEntityList = candidateRepository.findAll().stream()
                .filter(candidate -> candidate.getCvEntityList().stream()
                        .anyMatch(cv -> cv.getExperiences() != null &&
                                cv.getExperiences().toLowerCase().contains(experiences.toLowerCase().trim())))
                .toList();

        if (candidateEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        return candidateEntityList.stream()
                .map(CandidateMapper::mapToCandidateRespDto)
                .toList();
    }

    @Override
    public List<CandidateRespDto> findBySkills(String skills) {
        List<CandidateEntity> candidateEntityList = candidateRepository.findAll().stream()
                .filter(candidate -> candidate.getCvEntityList().stream()
                        .anyMatch(cv ->
                                cv.getSkills().toLowerCase().contains(skills.toLowerCase().trim())))
                .toList();

        if (candidateEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        return candidateEntityList.stream()
                .map(CandidateMapper::mapToCandidateRespDto)
                .toList();
    }

    private boolean checkIfItNeedsUpdating(CandidateReqDto candidateReqDto, CandidateEntity candidate) {
        boolean isUpdated = false;

        if (candidateReqDto.getName() != null && !Objects.equals(candidate.getName(),
                candidateReqDto.getName())) {
            candidate.setName(candidateReqDto.getName());
            isUpdated = true;
        }

        if (candidateReqDto.getSurname() != null && !Objects.equals(candidate.getSurname(),
                candidateReqDto.getSurname())) {
            candidate.setSurname(candidateReqDto.getSurname());
            isUpdated = true;
        }

        if (candidateReqDto.getAge() != null && !Objects.equals(candidate.getAge(),
                candidateReqDto.getAge())) {
            candidate.setAge(candidateReqDto.getAge());
            isUpdated = true;
        }

        if (candidateReqDto.getPhoneNumber() != null && !Objects.equals(candidate.getPhoneNumber(),
                candidateReqDto.getPhoneNumber())) {
            candidate.setPhoneNumber(candidateReqDto.getPhoneNumber());
            isUpdated = true;
        }

        return isUpdated;
    }
}
