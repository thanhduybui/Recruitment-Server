package com.edu.hcmute.dto;

import com.edu.hcmute.entity.Field;
import com.edu.hcmute.entity.Location;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class FindJobCvProfileDTO {
    private String gender;
    private Location workLocation;
    private Field field;
    private Field major;
}
