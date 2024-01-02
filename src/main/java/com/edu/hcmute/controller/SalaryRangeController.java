package com.edu.hcmute.controller;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.SalaryRangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/salary-ranges")
public class SalaryRangeController {
    private final SalaryRangeService salaryRangeService;
    @GetMapping
    public ResponseEntity<ResponseData> getAllSalaryRange(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all){
        ServiceResponse response = salaryRangeService.getAll(all);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getOneSalaryRange(@PathVariable("id") Integer id){
        ServiceResponse response = salaryRangeService.getOne(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> createSalaryRange(@RequestBody @Valid  OptionDTO salaryRange){
        ServiceResponse response = salaryRangeService.create(salaryRange);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> deleteSalaryRange(@PathVariable("id") Integer id){
        ServiceResponse response = salaryRangeService.delete(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

}
