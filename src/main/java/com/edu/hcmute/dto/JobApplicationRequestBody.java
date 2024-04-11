package com.edu.hcmute.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.edu.hcmute.constant.Message.*;

@Data
public class JobApplicationRequestBody {

    @JsonAlias("job_id")
    private Long jobId;

    @JsonAlias("cv_id")
    private int cvId;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = EMAIL_VALIDATION_ERROR)
    private String email;

    @Pattern(regexp = "^(\\+?\\d{1,3}[- ]?)?\\d{9,}$", message = PHONE_VALIDATION_ERROR)
    private String phone;

    @NotBlank
    @Size(max = 100, message = FULL_NAME_INVALID_LENGTH)
    private String name;

}
