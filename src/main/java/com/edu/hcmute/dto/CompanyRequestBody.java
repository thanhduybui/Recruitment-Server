package com.edu.hcmute.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.edu.hcmute.constant.Message.EMAIL_VALIDATION_ERROR;
import static com.edu.hcmute.constant.Message.PHONE_VALIDATION_ERROR;

@Data
public class CompanyRequestBody {
    @JsonAlias("name")
    @NotBlank
    private String name;
    @JsonAlias("description")
    @NotBlank
    private String description;
    @JsonAlias("scale")
    private String scale;
    @JsonAlias("address")
    private String address;
    @JsonAlias("email")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = EMAIL_VALIDATION_ERROR)
    private String email;
    @Pattern(regexp = "^(\\+?\\d{1,3}[- ]?)?\\d{9,}$", message = PHONE_VALIDATION_ERROR)
    @JsonAlias("phone")
    private String phone;
    @JsonAlias("web_url")
    private String webUrl;
}
