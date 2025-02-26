package it.giocode.cv_managment.controller;

import it.giocode.cv_managment.dto.req.candidate.CandidateReqDto;
import it.giocode.cv_managment.dto.req.candidate.UpdateCandidateReqDto;
import it.giocode.cv_managment.dto.resp.ResponseDto;
import it.giocode.cv_managment.dto.resp.candidate.CandidateRespDto;
import it.giocode.cv_managment.entity.UserEntity;
import it.giocode.cv_managment.repository.UserRepository;
import it.giocode.cv_managment.service.iface.ICandidateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CandidateController {

    private final ICandidateService candidateService;
    private final UserRepository userRepository;

    @PostMapping("/candidate/create/{userId}")
    public ResponseEntity<ResponseDto> createCandidate(@PathVariable Long userId,
            @Valid @RequestBody CandidateReqDto candidateReqDto) {

        boolean isCreated = candidateService.createCandidate(userId, candidateReqDto);

        if (!isCreated) {
            ResponseDto responseDto = ResponseDto.builder()
                    .statusCode(500)
                    .message("Something went wrong. Please try later")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        UserEntity user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            ResponseDto responseDto = ResponseDto.builder()
                    .statusCode(404)
                    .message("User not found")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }

        ResponseDto responseDto = ResponseDto.builder()
                .statusCode(201)
                .message("Candidate created successfully")
                .id(user.getUserId())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @PutMapping("/candidate/update/{candidateId}")
    public ResponseEntity<ResponseDto> updateCandidate(@PathVariable Long candidateId,
            @Valid @RequestBody UpdateCandidateReqDto updateCandidateReqDto) {

        ResponseDto responseDto;
        boolean isUpdated = candidateService.updateCandidate(candidateId, updateCandidateReqDto);

        if (!isUpdated) {
            responseDto = ResponseDto.builder()
                    .statusCode(500)
                    .message("Something went wrong. Please try later")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        responseDto = ResponseDto.builder()
                .statusCode(200)
                .message("Candidate updated successfully")
                .id(candidateId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/candidate/delete/{candidateId}")
    public ResponseEntity<ResponseDto> deleteCandidate(@PathVariable Long candidateId) {

        ResponseDto responseDto;
        boolean isDeleted = candidateService.deleteCandidate(candidateId);

        if (!isDeleted) {
            responseDto = ResponseDto.builder()
                    .statusCode(500)
                    .message("Something went wrong. Please try later")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        responseDto = ResponseDto.builder()
                .statusCode(200)
                .message("Candidate deleted successfully")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/candidate/byId/{candidateId}")
    public ResponseEntity<CandidateRespDto> getCandidateById(@PathVariable Long candidateId) {
        CandidateRespDto candidateRespDto = candidateService.findById(candidateId);

        return ResponseEntity.status(HttpStatus.OK).body(candidateRespDto);
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<CandidateRespDto>> getCandidatesByName(@RequestParam String name) {

        List<CandidateRespDto> candidateRespDtoList = candidateService.findByName(name);

        if (candidateRespDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(candidateRespDtoList);
    }

    @GetMapping("/candidates/experiences")
    public ResponseEntity<List<CandidateRespDto>> getCandidatesByExperiences(@RequestParam String experiences) {

        List<CandidateRespDto> candidateRespDtoList = candidateService.findByExperiences(experiences);

        if (candidateRespDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(candidateRespDtoList);
    }

    @GetMapping("/candidates/skills")
    public ResponseEntity<List<CandidateRespDto>> getCandidatesBySkills(@RequestParam String skills) {

        List<CandidateRespDto> candidateRespDtoList = candidateService.findBySkills(skills);

        if (candidateRespDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(candidateRespDtoList);
    }

    @GetMapping("/candidates/all")
    public ResponseEntity<List<CandidateRespDto>> getCandidatesAll() {

        List<CandidateRespDto> candidateRespDtoList = candidateService.findAll();

        if (candidateRespDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(candidateRespDtoList);
    }

    @GetMapping("/candidate/byUserId/{userId}")
    public ResponseEntity<CandidateRespDto> getCandidateByUserId(@PathVariable Long userId) {
        CandidateRespDto candidateRespDto = candidateService.findByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(candidateRespDto);
    }
}
