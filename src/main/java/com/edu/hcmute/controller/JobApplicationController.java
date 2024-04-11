package com.edu.hcmute.controller;

import com.edu.hcmute.dto.JobApplicationRequestBody;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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


}
