package com.edu.hcmute.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CvDTO {
    private Integer id;
    private String name;
    @JsonAlias("cv_url")
    private String cvUrl;
    @JsonAlias("is_default")
    private Boolean isDefault;
    @JsonAlias("created_at")
    private String createdAt;
    @JsonAlias("updated_at")
    private String updatedAt;
    @JsonAlias("created_by")
    private String createdBy;
    @JsonAlias("candidate_id")
    private Integer candidateId;
}
