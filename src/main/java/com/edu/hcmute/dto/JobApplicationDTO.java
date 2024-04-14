package com.edu.hcmute.dto;

import com.edu.hcmute.constant.JobApplicationStatus;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.CV;
import com.edu.hcmute.entity.Job;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.time.Instant;

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

    @JsonAlias("create_at")
    private String createAt;

    @JsonAlias("update_at")
    private String updateAt;

    @JsonAlias("apply_status")
    private JobApplicationStatus jobApplicationStatus;
}
