package com.edu.hcmute.controller;


import com.edu.hcmute.dto.JobFilterCriteria;
import com.edu.hcmute.dto.JobRequestBody;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    @PostMapping
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> createNewJob(@RequestBody @Valid JobRequestBody jobRequestBody) {
        ServiceResponse serviceResponse = jobService.create(jobRequestBody);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> updateJob(@PathVariable("id") Long id,
                                                  @RequestBody @Valid JobRequestBody jobRequestBody) {
        ServiceResponse serviceResponse = jobService.update(id, jobRequestBody);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getJobDetail(@PathVariable("id") Long id) {
        ServiceResponse serviceResponse = jobService.getOne(id);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> deleteJob(@PathVariable("id") Long id) {
        ServiceResponse serviceResponse = jobService.deleteJob(id);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @GetMapping
    public ResponseEntity<ResponseData> getAllJobs(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                    @RequestParam(value = "all", defaultValue = "false", required = false) Boolean all,
                                                     @RequestBody(required = false) @Valid JobFilterCriteria filter) {
       ServiceResponse serviceResponse = jobService.getAll(page, size, all, filter);
       return ResponseEntity.status(serviceResponse.getStatusCode())
               .body(ResponseData.builder()
                       .status(serviceResponse.getStatus())
                       .message(serviceResponse.getMessage())
                       .data(serviceResponse.getData())
                       .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData> searchJobs(){
        return null;
    }
}
