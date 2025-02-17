package it.giocode.cv_managment.dto.req.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserLoginReqDto {

    @NotNull(message = "Field email can not be null")
    @Email(message = "Field email must be a valid value")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@(gmail|aruba|libero|yahoo|outlook|hotmail|virgilio)\\.(com|it)$",
            message = "The email must be in the format: example@gmail.com or example@aruba.it"
    )
    private String email;

    @NotNull(message = "Field password can not be null")
    @Size(min = 5, max = 20, message = "The password must be between 5 and 15 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+])[A-Za-z\\d!@#$%^&*()\\-+]{5,20}$",
            message = "The password must contain at least one uppercase letter, one lowercase letter, one number " +
                    "and one special character between !@#$%^&*()-+"
    )
    private String password;
}
