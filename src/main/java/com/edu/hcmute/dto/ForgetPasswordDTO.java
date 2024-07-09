package com.edu.hcmute.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

import static com.edu.hcmute.constant.Message.EMAIL_VALIDATION_ERROR;

@Data
public class ForgetPasswordDTO implements Serializable {

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = EMAIL_VALIDATION_ERROR)
    private String email;
}
