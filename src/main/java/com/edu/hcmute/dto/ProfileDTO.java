package com.edu.hcmute.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import static com.edu.hcmute.constant.Message.*;

@Data
@Builder
public class ProfileDTO {
    @Pattern(regexp = "^[a-zA-Z\\s\\p{L}]*$", message = FULL_NAME_VALIDATION_ERROR)
    @Size(max = 100, message = FULL_NAME_INVALID_LENGTH)
    @NotBlank(message = FULL_NAME_NOT_EMPTY)
    @JsonAlias("full_name")
    private String fullName;

    @Pattern(regexp = "^(OTHER|FEMALE|MALE)$", message = INVALID_GENDER)
    private String gender;

    @Pattern(regexp = "^(\\+?\\d{1,3}[- ]?)?\\d{9,15}$", message = PHONE_VALIDATION_ERROR)
    @JsonAlias("phone_number")
    private String phoneNumber;

    private String email;
}
