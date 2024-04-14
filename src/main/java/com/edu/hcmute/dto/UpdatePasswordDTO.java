package com.edu.hcmute.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import static com.edu.hcmute.constant.Message.EMAIL_VALIDATION_ERROR;
import static com.edu.hcmute.constant.Message.PASSWORD_VALIDATION_ERROR;

@Data
@Builder
public class UpdatePasswordDTO {

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = PASSWORD_VALIDATION_ERROR)
    private String password;

    @NotBlank(message = "Confirm Password is required")
    @JsonAlias("confirm_password")
    private String confirmPassword;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = EMAIL_VALIDATION_ERROR)
    private String email;

    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}

