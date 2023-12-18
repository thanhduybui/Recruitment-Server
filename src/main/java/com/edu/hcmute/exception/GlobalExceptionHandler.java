package com.edu.hcmute.exception;


import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ResponseDataSatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ResponseData> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseData.builder()
                        .status(ResponseDataSatus.SUCCESS)
                        .message(errors.get(0)).build());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> globalExceptionHandler(Exception ex, WebRequest request) {
        ResponseData responseData = ResponseData.builder()
                .status(ResponseDataSatus.ERROR)
                .message(request.getDescription(false))
                .timestamp(new Date())
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseData);
    }
}
