package com.edu.hcmute.dto;

import com.edu.hcmute.service.auth.RegisterContainer;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

import static com.edu.hcmute.constant.Message.*;

@Data
@Builder
public class RegisterDTO implements Serializable, RegisterContainer {
    @Pattern(regexp = "^[a-zA-Z\\s\\p{L}]*$", message = FULL_NAME_VALIDATION_ERROR)
    @Size(max = 100, message = FULL_NAME_INVALID_LENGTH)
    @NotBlank(message = FULL_NAME_NOT_EMPTY)
    @JsonAlias("full_name")
    private String fullName;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = EMAIL_VALIDATION_ERROR)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = PASSWORD_VALIDATION_ERROR)
    private String password;

    @NotBlank(message = "Confirm Password is required")
    @JsonAlias("confirm_password")
    private String confirmPassword;

    @Pattern(regexp = "^(CANDIDATE|RECRUITER|ADMIN)$", message = INVALID_ROLE)
    private String role;
    public boolean isPasswordMatching() {
        return password == null || !password.equals(confirmPassword);
    }
}
