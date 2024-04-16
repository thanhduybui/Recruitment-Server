package com.edu.hcmute.dto;

import com.edu.hcmute.constant.JobApplyStatus;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class JobApplicationDTO {

    private Integer id;
    private String email;
    private String name;
    private String phone;

    @JsonAlias("cv_id")
    private CvDTO cv;

    @JsonAlias("job_id")
    private CandidateJobDTO job;

    @JsonAlias("created_at")
    private String createdAt;

    @JsonAlias("updated_at")
    private String updatedAt;

    @JsonAlias("status")
    private JobApplyStatus status;
}
