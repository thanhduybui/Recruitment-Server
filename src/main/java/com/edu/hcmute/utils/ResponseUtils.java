package com.edu.hcmute.utils;

import com.edu.hcmute.response.ServiceResponse;
import org.springframework.http.HttpStatus;

public class ResponseUtils {

    public static ServiceResponse responseFail(HttpStatus httpStatusCode, String status, String message) {

        return ServiceResponse.builder()
                .statusCode(httpStatusCode)
                .status(status)
                .message(message)
                .build();
    }
    public static ServiceResponse responseSuccess(HttpStatus httpStatusCode, String status, String message, Object data) {

        return ServiceResponse.builder()
                .statusCode(httpStatusCode)
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

}
