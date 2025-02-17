package it.giocode.cv_managment.controller;

import it.giocode.cv_managment.dto.req.cv.CVReqDto;
import it.giocode.cv_managment.dto.resp.ResponseDto;
import it.giocode.cv_managment.dto.resp.cv.CVRespDto;
import it.giocode.cv_managment.service.iface.ICVService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Validated
public class CVController {

    private final ICVService cvService;

    @PostMapping("/cv/create/{candidateId}")
    public ResponseEntity<ResponseDto> createCandidate(@PathVariable Long candidateId,
            @Valid @RequestBody CVReqDto cvReqDto) {

        ResponseDto responseDto;
        boolean isCreated = cvService.createCV(candidateId, cvReqDto);

        if (!isCreated) {
            responseDto = ResponseDto.builder()
                    .statusCode(500)
                    .message("Something went wrong. Please try later")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        responseDto = ResponseDto.builder()
                .statusCode(201)
                .message("CV created successfully")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/cv/update/{cvId}")
    public ResponseEntity<ResponseDto> updateCV(@PathVariable Long cvId,
            @Valid @RequestBody CVReqDto cvReqDto) {

        ResponseDto responseDto;
        boolean isUpdated = cvService.updateCV(cvId, cvReqDto);

        if (!isUpdated) {
            responseDto = ResponseDto.builder()
                    .statusCode(500)
                    .message("Something went wrong. Please try later")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        responseDto = ResponseDto.builder()
                .statusCode(200)
                .message("CV updated successfully")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/cv/delete/{candidateId}/{cvId}")
    public ResponseEntity<ResponseDto> deleteCandidate(@PathVariable Long candidateId, @PathVariable Long cvId) {

        ResponseDto responseDto;
        boolean isDeleted = cvService.deleteCV(candidateId, cvId);

        if (!isDeleted) {
            responseDto = ResponseDto.builder()
                    .statusCode(500)
                    .message("Something went wrong. Please try later")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        responseDto = ResponseDto.builder()
                .statusCode(200)
                .message("CV deleted successfully")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/cv/all")
    public ResponseEntity<List<CVRespDto>> findAll() {
        List<CVRespDto> cvRespDtoList = cvService.findAll();

        if (cvRespDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(cvRespDtoList);
    }

    @GetMapping("/cv/download/{fileName}")
    public ResponseEntity<Resource> downloadCV(@PathVariable String fileName) {
        Resource resource = cvService.downloadCV(fileName);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @PostMapping("/cv/upload/{candidateId}")
    public ResponseEntity<ResponseDto> uploadCV(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long candidateId,
            @RequestParam("cvTitle") String cvTitle,
            @RequestParam("education") String education,
            @RequestParam("spokenLanguage") String spokenLanguage,
            @RequestParam("skills") String skills,
            @RequestParam("experiences") String experiences,
            @RequestParam("fileName") String fileName) throws IOException {

        CVReqDto cvReqDto = CVReqDto.builder()
                .cvTitle(cvTitle)
                .education(education)
                .spokenLanguage(spokenLanguage)
                .skills(skills)
                .experiences(experiences)
                .fileName(fileName)
                .build();

        String savedFileName = cvService.saveCV(file, candidateId, cvReqDto);

        ResponseDto responseDto = ResponseDto.builder()
                .statusCode(201)
                .message("CV saved with name: " + savedFileName)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
