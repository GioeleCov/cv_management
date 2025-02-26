package it.giocode.cv_managment.dto.resp.cv;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CVRespDto {

    private Long cvId;
    private String cvTitle;
    private String education;
    private String spokenLanguage;
    private String skills;
    private String experiences;
    private String profileImage;
    private String fileName;
}
