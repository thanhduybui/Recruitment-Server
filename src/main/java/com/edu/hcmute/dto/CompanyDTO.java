package com.edu.hcmute.dto;

import lombok.Data;

@Data
public class CompanyDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer scale;
    private String branch;
    private String email;
    private String phone;
    private String webUrl;
    private String image;
    private String status;
}
