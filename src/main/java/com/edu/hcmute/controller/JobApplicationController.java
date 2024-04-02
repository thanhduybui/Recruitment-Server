package com.edu.hcmute.controller;

import com.edu.hcmute.dto.JobApplicationRequestBody;
import com.edu.hcmute.entity.JobApplication;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-application")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<ResponseData> createNewJobApplication(@RequestBody @Valid JobApplicationRequestBody jobApplicationRequestBody) {

        ServiceResponse serviceResponse = jobApplicationService.createJobApplication(jobApplicationRequestBody);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                .status(serviceResponse.getStatus())
                .message(serviceResponse.getMessage())
                .data(serviceResponse.getData())
                .build());
    }

    @GetMapping("/get-all")
    public ResponseEntity<ResponseData> getAllJobApplication() {
        ServiceResponse serviceResponse = jobApplicationService.getAll();
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData()).build());
    }



//    @GetMapping("/get-by-user/{id}")
//    public ResponseEntity<ResponseData> getByUser(@PathVariable("id") Long userId) {
//        ServiceResponse serviceResponse = jobApplicationService.getByUser(userId);
//        return ResponseEntity.status(serviceResponse.getStatusCode())
//                .body(ResponseData.builder()
//                        .status(serviceResponse.getStatus())
//                        .message(serviceResponse.getMessage())
//                        .data(serviceResponse.getData()).build());
//    }
}
