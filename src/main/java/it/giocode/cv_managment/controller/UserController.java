package it.giocode.cv_managment.controller;

import it.giocode.cv_managment.dto.req.user.UserCreationReqDto;
import it.giocode.cv_managment.dto.req.user.UserLoginReqDto;
import it.giocode.cv_managment.dto.resp.ResponseDto;
import it.giocode.cv_managment.service.iface.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<ResponseDto> userRegistration(@Valid @RequestBody UserCreationReqDto userCreationReqDto) {

        ResponseDto responseDto;
        boolean isRegistered = userService.userRegistration(userCreationReqDto);

        if (!isRegistered) {
            responseDto = ResponseDto.builder()
                    .statusCode(500)
                    .message("Something went wrong. Please try later")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        responseDto = ResponseDto.builder()
                .statusCode(201)
                .message("User created successfully")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDto> userLogin(@Valid @RequestBody UserLoginReqDto userLoginReqDto) {

        ResponseDto responseDto;
        boolean isLogged = userService.userLogin(userLoginReqDto);

        if (!isLogged) {
            responseDto = ResponseDto.builder()
                    .statusCode(500)
                    .message("Something went wrong. Please try later")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }

        responseDto = ResponseDto.builder()
                .statusCode(200)
                .message("User logged in successfully")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
