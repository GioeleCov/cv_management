package it.giocode.cv_managment.service.impl;

import it.giocode.cv_managment.dto.req.cv.CVReqDto;
import it.giocode.cv_managment.dto.req.cv.UpdateCVReqDto;
import it.giocode.cv_managment.dto.resp.cv.CVRespDto;
import it.giocode.cv_managment.entity.CVEntity;
import it.giocode.cv_managment.entity.CandidateEntity;
import it.giocode.cv_managment.exception.exception_class.AlreadyExistsException;
import it.giocode.cv_managment.exception.exception_class.NotFoundException;
import it.giocode.cv_managment.mapper.CVMapper;
import it.giocode.cv_managment.repository.CVRepository;
import it.giocode.cv_managment.repository.CandidateRepository;
import it.giocode.cv_managment.service.iface.ICVService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CVServiceImpl implements ICVService {

    private final CVRepository cvRepository;
    private final CandidateRepository candidateRepository;
    private final PdfService pdfService;
    private final String path = "C://Users//CORSO_JJ06\\Desktop//working_area//projekt2.0//backend//cv-management//src//main//resources//static//cv/";

    @Override
    public boolean createCV(Long candidateId, CVReqDto cvReqDto) {

        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate", "id", candidateId.toString()));

        if (checkIfCVTitleAlreadyExists(cvReqDto, candidate)) {
            throw new AlreadyExistsException("CV", cvReqDto.getFileName());
        }

        CVEntity cv = CVMapper.mapToCVEntity(cvReqDto);
        cv.setCandidate(candidate);
        candidate.getCvEntityList().add(cv);

        cv.setCreatedAt(LocalDateTime.now());
        pdfService.generatePdf(candidate, cv, path);
        cvRepository.save(cv);
        return true;
    }

    @Override
    public boolean updateCV(Long cvId, UpdateCVReqDto updateCVReqDto) {
        CVEntity cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new NotFoundException("CV", "id", cvId.toString()));

        CVReqDto cvReqDto = CVMapper.mapUpdateReqDtoToCVReqDto(updateCVReqDto);

        boolean updated = checkIfItNeedsUpdating(cv, cvReqDto);

        if (updated) {
            cv.setUpdatedAt(LocalDateTime.now());
            String oldFilePath = Paths.get(this.path, cv.getFileName() + ".pdf").toString();

            try {
                Files.deleteIfExists(Paths.get(oldFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            pdfService.generatePdf(cv.getCandidate(), cv, path);
            cvRepository.save(cv);
        }

        return updated;
    }

    @Override
    public boolean deleteCV(Long candidateId, Long cvId) {
        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate", "id", candidateId.toString()));

        CVEntity cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new NotFoundException("CV", "id", cvId.toString()));

        candidate.getCvEntityList().remove(cv);

        try {
            Path oldPath = Paths.get(path + cv.getFileName());
            Files.delete(oldPath);
        }catch (IOException e) {
            e.printStackTrace();
        }

        cvRepository.delete(cv);

        return true;
    }

    @Override
    public List<CVRespDto> findAll() {
        List<CVEntity> cvEntityList = cvRepository.findAll();

        if (cvEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        return cvEntityList.stream()
                .map(CVMapper::mapToCVRespDto)
                .toList();
    }

    @Override
    public Resource downloadCV(String fileName) {

        try {
            String filePath = this.path + fileName;
            Path path = Paths.get(filePath);
            File file = path.toFile();

            if (!file.exists()) {
                return null;
            }

            return new FileSystemResource(file);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String saveCV(MultipartFile file, Long candidateId, CVReqDto cvReqDto) throws IOException {
        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate", "id", candidateId.toString()));

        CVEntity cv = CVMapper.mapToCVEntity(cvReqDto);

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new IOException("File must be a pdf");
        }

        String fileName = cv.getFileName();

        if (checkIfCVTitleAlreadyExists(cvReqDto, candidate)) {
            throw new AlreadyExistsException("CV", fileName);
        }

        Path filePath = Paths.get(path + fileName);

        Files.copy(file.getInputStream(), filePath);

        cv.setCandidate(candidate);
        candidate.getCvEntityList().add(cv);
        cv.setCreatedAt(LocalDateTime.now());
        cvRepository.save(cv);

        return fileName;
    }

    private boolean checkIfCVTitleAlreadyExists(CVReqDto cvReqDto, CandidateEntity candidate) {
        CVEntity cvEntity = candidate.getCvEntityList().stream()
                .filter(cv  -> cv.getFileName().equals(cvReqDto.getFileName()))
                .findFirst()
                .orElse(null);

        return cvEntity != null;
    }

    private boolean checkIfItNeedsUpdating(CVEntity cv, CVReqDto cvReqDto) {
        boolean isUpdated = false;

        if (cvReqDto.getCvTitle() != null && !Objects.equals(cv.getCvTitle(), cvReqDto.getCvTitle())) {
            cv.setCvTitle(cvReqDto.getCvTitle());
            isUpdated = true;
        }

        if (cvReqDto.getEducation() != null && !Objects.equals(cv.getEducation(), cvReqDto.getEducation())) {
            cv.setEducation(cvReqDto.getEducation());
            isUpdated = true;
        }

        if (cvReqDto.getSkills() != null && !Objects.equals(cv.getSkills(), cvReqDto.getSkills())) {
            cv.setSkills(cvReqDto.getSkills());
            isUpdated = true;
        }

        if (cvReqDto.getExperiences() != null && !Objects.equals(cv.getExperiences(), cvReqDto.getExperiences())) {
            cv.setExperiences(cvReqDto.getExperiences());
            isUpdated = true;
        }

        if (cvReqDto.getProfileImage() != null && !Objects.equals(cv.getProfileImage(), cvReqDto.getProfileImage())) {
            cv.setProfileImage(cvReqDto.getProfileImage());
            isUpdated = true;
        }

        if (cvReqDto.getFileName() != null && !Objects.equals(cv.getFileName(), cvReqDto.getFileName())) {
            cv.setFileName(cvReqDto.getFileName());
            isUpdated = true;
        }

        return isUpdated;
    }
}
