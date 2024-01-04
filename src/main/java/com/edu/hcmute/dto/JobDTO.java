package com.edu.hcmute.dto;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.entity.*;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
public class JobDTO {
    private Long id;

    private String title;

    private String description;

    private String requirement;

    private String benefit;

    private Integer slots;

    private Integer view;

    @JsonAlias("apply_number")
    private Integer applyNumber;

    @JsonAlias("like_number")
    private Integer likeNumber;

    private Status status;

    private Instant deadline;

    @JsonAlias("work_location")
    private String workLocation;

    @JsonAlias("work_time")
    private String workTime;


    @JsonAlias("created_at")
    private Instant createdAt;

    @JsonAlias("updated_at")
    private Instant updatedAt;

    @JsonAlias("created_by")
    private String createdBy;

    @JsonAlias("is_hot")
    private Boolean isHot;

    private SalaryRange salaryRange;

    private Company company;

    private Position position;

    private Major major;

    private Field field;

    private ExperienceRange experienceRange;

}
