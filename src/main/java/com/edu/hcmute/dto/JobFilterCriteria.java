package com.edu.hcmute.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;


@Builder
@Setter
public class JobFilterCriteria {
    private String keyword;
    @JsonAlias("location_id")
    private Integer locationId;
    @JsonAlias("work_mode_id")
    private Integer workModeId;
    @JsonAlias("field_id")
    private Integer fieldId;
    @JsonAlias("major_id")
    private Integer majorId;
    @JsonAlias("salary_id")
    private Integer salaryId;
    @JsonAlias("experience_id")
    private Integer experienceId;
    @JsonAlias("position_id")
    private Integer positionId;
    @JsonAlias("is_hot")
    private Boolean isHot;

    public String getKeyword() {
        return keyword.isEmpty() ? null : keyword;
    }

    public Integer getLocationId() {
        return locationId <= 0 ? null : locationId;
    }

    public Integer getWorkModeId() {
        return workModeId <= 0 ? null : workModeId;
    }

    public Integer getFieldId() {
        return fieldId <= 0 ? null : fieldId;
    }

    public Integer getMajorId() {
        return majorId <= 0 ? null : majorId;
    }

    public Integer getSalaryId() {
        return salaryId <= 0 ? null : salaryId;
    }

    public Integer getExperienceId() {
        return experienceId <= 0 ? null : experienceId;
    }

    public Integer getPositionId() {
        return positionId <= 0 ? null : positionId;
    }

    public Boolean getHot() {
        return isHot;
    }
}
