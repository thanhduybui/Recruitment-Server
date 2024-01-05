package com.edu.hcmute.controller;


import com.edu.hcmute.dto.CompanyRequestBody;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/profile")
    public ResponseEntity<ResponseData> getCompanyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ServiceResponse response = companyService.getCompanyForUser(email);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @PutMapping("/profile")
    public ResponseEntity<ResponseData> updateCompanyProfile(@RequestBody @Valid CompanyRequestBody companyRequestBody) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ServiceResponse response = companyService.updateCompanyForUser(email, companyRequestBody);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }
}
