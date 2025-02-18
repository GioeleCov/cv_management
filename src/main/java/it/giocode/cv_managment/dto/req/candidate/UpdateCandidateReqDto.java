package it.giocode.cv_managment.dto.req.candidate;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UpdateCandidateReqDto {

    private String name;
    private String surname;
    private Integer age;
    private String phoneNumber;
}
