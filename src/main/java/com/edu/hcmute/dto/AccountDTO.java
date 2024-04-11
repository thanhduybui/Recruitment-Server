package com.edu.hcmute.dto;

import com.edu.hcmute.constant.Status;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.edu.hcmute.constant.Message.*;


@Data
public class AccountDTO {

    private Long id;

    @Pattern(regexp = "^[a-zA-Z\\s\\p{L}]*$", message = FULL_NAME_VALIDATION_ERROR)
    @Size(max = 100, message = FULL_NAME_INVALID_LENGTH)
    @NotBlank(message = FULL_NAME_NOT_EMPTY)
    @JsonAlias("full_name")
    private String fullName;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = EMAIL_VALIDATION_ERROR)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = PASSWORD_VALIDATION_ERROR)
    private String password;

    @Pattern(regexp = "^(CANDIDATE|EMPLOYER|ADMIN)$", message = INVALID_ROLE)
    private String role;

    @Pattern(regexp = "^(ACTIVE|INACTIVE|LOCK|BAN)$", message = INVALID_STATUS)
    private String status;

}
