package it.giocode.cv_managment.mapper;

import it.giocode.cv_managment.dto.req.candidate.CandidateReqDto;
import it.giocode.cv_managment.dto.resp.candidate.CandidateRespDto;
import it.giocode.cv_managment.dto.resp.cv.CVRespDto;
import it.giocode.cv_managment.entity.CandidateEntity;

import java.util.List;

public class CandidateMapper {

    private CandidateMapper() {

    }

    private static List<CVRespDto> mapToCVRespDtoList(CandidateEntity candidate) {
        return candidate.getCvEntityList().stream()
                .map(CVMapper::mapToCVRespDto)
                .toList();
    }

    public static CandidateEntity mapToCandidateEntity(CandidateReqDto candidateReqDto) {
        return CandidateEntity.builder()
                .name(candidateReqDto.getName())
                .surname(candidateReqDto.getSurname())
                .age(candidateReqDto.getAge())
                .phoneNumber(candidateReqDto.getPhoneNumber())
                .build();
    }

    public static CandidateRespDto mapToCandidateRespDto(CandidateEntity candidate) {
        List<CVRespDto> cvRespDtoList = CandidateMapper.mapToCVRespDtoList(candidate);

        return CandidateRespDto.builder()
                .name(candidate.getName())
                .surname(candidate.getSurname())
                .age(candidate.getAge())
                .phoneNumber(candidate.getPhoneNumber())
                .cvRespDtoList(cvRespDtoList)
                .build();
    }
}
