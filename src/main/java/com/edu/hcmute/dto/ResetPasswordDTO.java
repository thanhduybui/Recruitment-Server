package com.edu.hcmute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.edu.hcmute.constant.Message.PASSWORD_VALIDATION_ERROR;

@Data
public class ResetPasswordDTO {
    @NotBlank(message = "Old Password is required")
    private String old_password;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = PASSWORD_VALIDATION_ERROR)
    private String new_password;

    @NotBlank(message = "Confirm Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = PASSWORD_VALIDATION_ERROR)
    private String confirm_new_password;

    public boolean isPasswordMatching() {
        return new_password == null || !new_password.equals(confirm_new_password);
    }
}
