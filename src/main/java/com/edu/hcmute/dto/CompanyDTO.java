package com.edu.hcmute.dto;

import lombok.Data;

@Data
public class CompanyDTO {
    private Integer id;
    private String name;
    private String description;
    private String scale;
    private String address;
    private String email;
    private String phone;
    private String webUrl;
    private String image;
    private String status;
    private String businessLicense;
}
