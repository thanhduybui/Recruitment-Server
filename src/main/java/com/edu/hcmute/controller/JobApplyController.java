package com.edu.hcmute.controller;


import com.edu.hcmute.dto.JobApplyDTO;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.JobApplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job-apply")
@RequiredArgsConstructor
public class JobApplyController {
    private final JobApplyService jobApplyService;
    @PostMapping
    public ResponseEntity<ResponseData> applyJob(@RequestBody @Valid JobApplyDTO jobApplyDTO) {
        ServiceResponse response = jobApplyService.create(jobApplyDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }
}
