package com.edu.hcmute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.edu.hcmute.constant.Message.INVALID_ROLE;

@Data
public class TokenDTO {

    @NotBlank(message = "Token không hợp lệ")
    private String token;

    @Pattern(regexp = "^(CANDIDATE|EMPLOYER|ADMIN)$", message = INVALID_ROLE)
    private String role;
}
