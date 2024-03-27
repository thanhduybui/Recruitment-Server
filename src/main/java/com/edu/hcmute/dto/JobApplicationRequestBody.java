package com.edu.hcmute.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobApplicationRequestBody {

    @JsonAlias("candidate_id")
    private Long userId;

    @JsonAlias("job_id")
    private Long jobId;

    @JsonAlias("cv_id")
    private int cvId;

    @NotBlank
    private String email;

    private String phone;

    @NotBlank
    private String name;

}
