package com.edu.hcmute.dto;


import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class CvDTO {
    private Long id;
    private String name;
    private String cvUrl;

    private Boolean isDefault;

    private Instant createAt;
    private Instant updateAt;
}
