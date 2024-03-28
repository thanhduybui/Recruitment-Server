package com.edu.hcmute.dto;

import com.edu.hcmute.constant.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FavoriteJobDTO {

    @Pattern(regexp = "^[0-9]$", message = Message.INVALID_JOB)
    @NotNull(message = Message.INVALID_JOB)
    private Long jobId;
}
