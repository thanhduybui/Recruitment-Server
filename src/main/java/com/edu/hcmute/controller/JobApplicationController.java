package com.edu.hcmute.controller;

import com.edu.hcmute.dto.JobApplicationRequestBody;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.job.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/job-applications")
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

    @GetMapping("/get-all-by-job")
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> getAllJobApplicationByJob(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                                  @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                                  @RequestParam(value = "jobId") Long jobId) {
        ServiceResponse serviceResponse = jobApplicationService.getAllJobApplicationByJob(page, size, jobId);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @PreAuthorize("hasAnyAuthority('CANDIDATE')")
    @GetMapping("/candidate")
    public ResponseEntity<ResponseData> getAllJobApplicationByCandidate() {
        ServiceResponse serviceResponse = jobApplicationService.getAllByCandidate();
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData()).build());
    }
}

