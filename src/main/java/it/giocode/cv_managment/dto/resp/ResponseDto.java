package it.giocode.cv_managment.dto.resp;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDto {

    private int statusCode;
    private String message;
}
