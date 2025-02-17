package it.giocode.cv_managment.dto.req.cv;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CVReqDto {

    @NotBlank(message = "Field cv title can not be null or empty.")
    private String cvTitle;

    @NotBlank(message = "Field education can not be null or empty.")
    private String education;

    @NotBlank(message = "Field spoken language can not be null or empty.")
    private String spokenLanguage;

    @NotBlank(message = "Field skills can not be null or empty.")
    private String skills;

    @NotBlank(message = "Field experiences can not be null or empty.")
    private String experiences;

    private String profileImage;

    @NotBlank(message = "Field file name can not be null or empty.")
    private String fileName;
}
