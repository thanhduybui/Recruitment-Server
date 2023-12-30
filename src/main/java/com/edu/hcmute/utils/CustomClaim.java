package com.edu.hcmute.utils;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomClaim {
    private String email;
    private String role;
}
