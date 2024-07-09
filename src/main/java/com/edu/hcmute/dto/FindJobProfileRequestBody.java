package com.edu.hcmute.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FindJobProfileRequestBody {
    private String gender;
    private Integer fieldId;
    private Integer majorId;
    private Integer locationId;
}
