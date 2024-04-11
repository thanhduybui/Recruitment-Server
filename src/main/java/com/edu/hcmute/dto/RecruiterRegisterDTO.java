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
import static com.edu.hcmute.constant.Message.INVALID_ROLE;

@Data
@Builder
public class RecruiterRegisterDTO implements Serializable, RegisterContainer {

    @Pattern(regexp = "^[a-zA-Z\\s\\p{L}]*$", message = FULL_NAME_VALIDATION_ERROR)
    @Size(max = 100, message = FULL_NAME_INVALID_LENGTH)
    @NotBlank(message = FULL_NAME_NOT_EMPTY)
    @JsonAlias("full_name")
    private String fullName;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = EMAIL_VALIDATION_ERROR)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = PASSWORD_VALIDATION_ERROR)
    private String password;

    @NotBlank(message = "Mật khẩu xác nhận không được để trống")
    @JsonAlias("confirm_password")
    private String confirmPassword;

    @Pattern(regexp = "^(CANDIDATE|RECRUITER|ADMIN)$", message = INVALID_ROLE)
    private String role;

    @Pattern(regexp = "^(\\+?\\d{1,3}[- ]?)?\\d{9,}$", message = PHONE_VALIDATION_ERROR)
    private String phone;

    @NotBlank(message = "Tên công ty không được để trống")
    @JsonAlias("company_name")
    private String companyName;

    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = INVALID_ROLE)
    private String gender;
    public boolean isPasswordMatching() {
        return password == null || !password.equals(confirmPassword);
    }
}
