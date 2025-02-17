package it.giocode.cv_managment.dto.resp;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ErrorResponseDto {

    private int errorCode;
    private String errorMessage;
    private LocalDateTime errorTime;
}
