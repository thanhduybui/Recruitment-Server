package com.edu.hcmute.exception;


import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ResponseDataStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String INVALID_ARGUMENT = "Tham số đường dẫn không hợp lệ";
    private static final String FORBIDDEN = "Không có quyền truy cập";
    private static final String NOT_DEFINED_ERROR = "Lỗi không xác định khi xử lý yêu cầu";

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
                        .status(ResponseDataStatus.ERROR)
                        .message(errors.get(0)).build());
    }

    @ExceptionHandler({DuplicateEntryException.class})
    public ResponseEntity<ResponseData> handleDuplicateEntryException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseData.builder()
                        .status(ResponseDataStatus.ERROR)
                        .message(ex.getMessage())
                        .build());
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ResponseData> handleMethodArgumentTypeMismatch(Exception ex) {
        return ResponseEntity.badRequest().body(
                ResponseData.builder()
                        .status(ResponseDataStatus.ERROR)
                        .message(INVALID_ARGUMENT).build());
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ResponseData> handleResourceNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ResponseData.builder()
                        .status(ResponseDataStatus.ERROR)
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseData> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ResponseData.builder()
                        .status(ResponseDataStatus.ERROR).message(FORBIDDEN).build());
    }

    @ExceptionHandler(UndefinedException.class)
    public ResponseEntity<ResponseData> handleUndefinedException(UndefinedException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseData.builder()
                        .status(ResponseDataStatus.ERROR)
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> globalExceptionHandler(Exception ex, WebRequest request) {
        System.out.println(ex.getMessage());
        ResponseData responseData = ResponseData.builder()
                .status(ResponseDataStatus.ERROR)
                .message(NOT_DEFINED_ERROR)
                .timestamp(new Date())
                .build();
        log.error("Error: ", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseData);
    }
}
