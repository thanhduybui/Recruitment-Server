package com.edu.hcmute.dto;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.entity.*;
import com.edu.hcmute.response.ShortData;
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

    private String deadline;

    @JsonAlias("work_location")
    private String workLocation;

    @JsonAlias("work_time")
    private String workTime;

    @JsonAlias("created_at")
    private String createdAt;

    @JsonAlias("updated_at")
    private String updatedAt;

    @JsonAlias("created_by")
    private String createdBy;

    @JsonAlias("is_hot")
    private Boolean isHot;

    private ShortData salaryRange;

    private CompanyDTO company;

    private Integer restAppliedDays;

    private ShortData position;

    private ShortData major;

    private ShortData location;

    private ShortData field;

    private ShortData experienceRange;

    private ShortData workMode;

}
