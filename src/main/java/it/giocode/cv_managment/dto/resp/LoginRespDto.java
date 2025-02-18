package it.giocode.cv_managment.dto.resp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginRespDto {

    private int statusCode;
    private String token;
    private String message;
}
