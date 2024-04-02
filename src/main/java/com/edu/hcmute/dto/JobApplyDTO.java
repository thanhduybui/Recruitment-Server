package com.edu.hcmute.dto;


import jakarta.validation.constraints.Pattern;
import liquibase.datatype.DataTypeInfo;
import lombok.Builder;
import lombok.Data;

import static com.edu.hcmute.constant.Message.*;


@Data
@Builder
public class JobApplyDTO {
    @Pattern(regexp = "^[a-zA-Z\\s\\p{L}]*$", message = FULL_NAME_VALIDATION_ERROR)
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = EMAIL_VALIDATION_ERROR)
    private String email;
    @Pattern(regexp = "^(\\+?\\d{1,3}[- ]?)?\\d{9,15}$", message = PHONE_VALIDATION_ERROR)
    private String phone;
    private Long jobId;
    private Long cvId;
}
