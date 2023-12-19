package com.edu.hcmute.dto;


import com.edu.hcmute.constant.Message;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyDTO {
    @Pattern(regexp = "^[0-9]{6}$", message = Message.OTP_VALIDATION_ERROR)
    private String otp;
}
