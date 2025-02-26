package it.giocode.cv_managment.dto.resp.candidate;

import it.giocode.cv_managment.dto.resp.cv.CVRespDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CandidateRespDto {

    private Long candidateId;
    private String name;
    private String surname;
    private String email;
    private Integer age;
    private String phoneNumber;
    private List<CVRespDto> cvRespDtoList;
}
