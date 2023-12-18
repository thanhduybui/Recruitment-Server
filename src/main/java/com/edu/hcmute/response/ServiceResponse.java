package com.edu.hcmute.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ServiceResponse {
    private HttpStatus statusCode;
    private String status;
    private String message;
    private Object data;
}

