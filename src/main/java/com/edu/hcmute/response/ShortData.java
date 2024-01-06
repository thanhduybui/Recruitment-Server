package com.edu.hcmute.response;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShortData {
    private Integer id;
    private String name;
}
