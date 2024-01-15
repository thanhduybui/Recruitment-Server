package com.edu.hcmute.dto;


import com.edu.hcmute.constant.Status;
import lombok.Builder;
import lombok.Data;
import org.hibernate.usertype.StaticUserTypeSupport;

@Data
@Builder
public class CandidateJobDTO {
    private Integer id;
    private String title;
    private Boolean isHot;
    private String companyName;
    private String salaryRange;
    private String locationId;
    private Boolean isFavorite;
    private Integer restAppliedDays;
    private String companyImage;
    private Status status;
}