package com.edu.hcmute.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String avatar;
    private String role;
    private String status;
    private String createdDate;
    private String updatedDate;
}
