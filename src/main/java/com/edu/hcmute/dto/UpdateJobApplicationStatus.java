package com.edu.hcmute.dto;

import com.edu.hcmute.constant.JobApplyStatus;
import lombok.Data;


@Data
public class UpdateJobApplicationStatus {
    private JobApplyStatus status;
}
