package com.edu.hcmute.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class CvRequestBody {
    private String name;
    private Boolean isDefault;
    private MultipartFile cvFile;
}
