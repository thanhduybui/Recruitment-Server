package com.edu.hcmute.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusinessLicenseDTO {
    private boolean isVerified;
    private String businessLicense;
}
