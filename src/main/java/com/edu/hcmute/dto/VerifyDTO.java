package com.edu.hcmute.dto;


import com.edu.hcmute.constant.Message;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.edu.hcmute.constant.Message.EMAIL_VALIDATION_ERROR;

@Data
public class VerifyDTO {
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = EMAIL_VALIDATION_ERROR)
    private String email;
    @Pattern(regexp = "^[0-9]{6}$", message = Message.OTP_VALIDATION_ERROR)
    private String otp;
}
