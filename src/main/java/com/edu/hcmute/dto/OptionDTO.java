package com.edu.hcmute.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
public class OptionDTO implements Serializable {
    private Integer id;
    @NotBlank
    private String name;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;
}
