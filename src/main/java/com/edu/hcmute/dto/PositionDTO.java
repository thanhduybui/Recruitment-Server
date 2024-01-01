package com.edu.hcmute.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PositionDTO {
    private Integer id;
    @NotBlank
    private String name;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;
}
