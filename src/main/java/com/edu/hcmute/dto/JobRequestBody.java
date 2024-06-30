package com.edu.hcmute.dto;



import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.Instant;
import java.util.List;

import static com.edu.hcmute.constant.Message.EMAIL_VALIDATION_ERROR;

@Data
public class JobRequestBody {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String requirement;

    @NotBlank
    private String benefit;

    @NotNull(message = "Số lượng ứng tuyển không được để trống")
    @Min(value = 1, message = "Số lượng ứng tuyển phải là số nguyên dương")
    private Integer slots;

    private String deadline;

    @JsonAlias("work_location")
    private String workLocation;

    @JsonAlias("work_time")
    private String workTime;

    @JsonAlias("is_hot")
    private Boolean isHot;

    @JsonAlias("location_id")
    private Integer locationId;

    @JsonAlias("salary_id")
    private Integer salaryId;

    @JsonAlias("company_id")
    private Integer companyId;

    @JsonAlias("position_id")
    private Integer positionId;

    @JsonAlias("work_mode_id")
    private Integer workModeId;

    @JsonAlias("major_id")
    private Integer majorId;

    @JsonAlias("field_id")
    private Integer fieldId;

    @JsonAlias("experience_id")
    private Integer experienceId;

    @JsonAlias("skill_ids")
    private List<Integer> skillIds;
}
