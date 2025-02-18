package it.giocode.cv_managment.mapper;

import it.giocode.cv_managment.dto.req.cv.CVReqDto;
import it.giocode.cv_managment.dto.req.cv.UpdateCVReqDto;
import it.giocode.cv_managment.dto.resp.cv.CVRespDto;
import it.giocode.cv_managment.entity.CVEntity;

import java.util.UUID;

public class CVMapper {

    private CVMapper() {

    }

    public static CVEntity mapToCVEntity(CVReqDto cvReqDto) {
        return CVEntity.builder()
                .cvTitle(cvReqDto.getCvTitle())
                .education(cvReqDto.getEducation())
                .spokenLanguage(cvReqDto.getSpokenLanguage())
                .skills(cvReqDto.getSkills())
                .experiences(cvReqDto.getExperiences())
                .profileImage(cvReqDto.getProfileImage())
                .fileName(cvReqDto.getFileName() + ".pdf")
                .build();
    }

    public static CVRespDto mapToCVRespDto(CVEntity cv) {
        return CVRespDto.builder()
                .cvTitle(cv.getCvTitle())
                .education(cv.getEducation())
                .spokenLanguage(cv.getSpokenLanguage())
                .skills(cv.getSkills())
                .experiences(cv.getExperiences())
                .profileImage(cv.getProfileImage())
                .fileName(cv.getFileName())
                .build();
    }

    public static CVReqDto mapUpdateReqDtoToCVReqDto(UpdateCVReqDto updateCVReqDto) {
        return CVReqDto.builder()
                .cvTitle(updateCVReqDto.getCvTitle())
                .education(updateCVReqDto.getEducation())
                .spokenLanguage(updateCVReqDto.getSpokenLanguage())
                .skills(updateCVReqDto.getSkills())
                .experiences(updateCVReqDto.getExperiences())
                .profileImage(updateCVReqDto.getProfileImage())
                .fileName(updateCVReqDto.getFileName())
                .build();
    }
}
