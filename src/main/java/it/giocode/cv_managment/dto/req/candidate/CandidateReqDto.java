package it.giocode.cv_managment.dto.req.candidate;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CandidateReqDto {

    @NotBlank(message = "Field name can not be null or empty")
    @Size(min = 2, max = 50, message = "Field name must be between 2 and 50 characters long")
    private String name;

    @NotBlank(message = "Field surname can not be null or empty")
    @Size(min = 2, max = 50, message = "Field surname must be between 2 and 50 characters long")
    private String surname;

    @NotNull(message = "Field surname can not be null or empty")
    @Min(value = 18, message = "Field age must contain a value greater than or equal to 18")
    @Max(value = 70, message = "Field must contain a value less than or equal to 708")
    private Integer age;

    @NotNull(message = "Field phone number can not be null")
    @Size(min = 15, max = 15, message = "The phone number must be exactly 15 characters")
    @Pattern(
            regexp = "^\\+39\\s3[0-9]{2}\\s[0-9]{7}$",
            message = "The telephone number must be in the format: +39 XXX XXXXXXX"
    )
    private String phoneNumber;
}
