package com.edu.hcmute.dto;

import com.edu.hcmute.constant.JobApplicationStatus;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.CV;
import com.edu.hcmute.entity.Job;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.time.Instant;

@Data
public class GetJobApplicationDTO {

    private Integer id;
    private String email;
    private String name;
    private String phone;
    private Long cvId;
    private Long jobId;
    private Instant createAt;
    private Instant updateAt;
    private JobApplicationStatus jobApplicationStatus;
}
