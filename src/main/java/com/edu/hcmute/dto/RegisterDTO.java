package com.edu.hcmute.dto;


import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import static com.edu.hcmute.constant.Message.*;

@Data
@Builder
public class RegisterDTO {
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = EMAIL_VALIDATION_ERROR)
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = PASSWORD_VALIDATION_ERROR)
    private String password;
    @NotBlank(message = "Confirm Password is required")
    private String confirmPassword;
    @Pattern(regexp = "^(CANDIDATE|EMPLOYER|ADMIN)$", message = INVALID_ROLE)
    private String role;

    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
