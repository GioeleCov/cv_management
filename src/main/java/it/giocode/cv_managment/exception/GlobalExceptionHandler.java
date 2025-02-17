package it.giocode.cv_managment.exception;

import it.giocode.cv_managment.dto.resp.ErrorResponseDto;
import it.giocode.cv_managment.exception.exception_class.AlreadyExistsException;
import it.giocode.cv_managment.exception.exception_class.IncorrectPasswordsException;
import it.giocode.cv_managment.exception.exception_class.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleAlreadyExistsException(AlreadyExistsException ex) {

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .errorCode(400)
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex) {

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .errorCode(404)
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectPasswordsException.class)
    public ResponseEntity<ErrorResponseDto> handleIncorrectPasswordsException(IncorrectPasswordsException ex) {

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .errorCode(400)
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
