package com.edu.hcmute.controller;


import com.edu.hcmute.dto.CompanyRequestBody;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/{id}/jobs")
    public ResponseEntity<ResponseData> getCompanyJobs(@PathVariable("id") Integer id,
                                                       @RequestParam(value = "type", defaultValue = "ACTIVE", required = false) String type,
                                                       @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                       @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        ServiceResponse response = companyService.getCompanyJobs(id, page, size, type);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @GetMapping
    public ResponseEntity<ResponseData> getAllCompany(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                      @RequestParam(value = "size", defaultValue = "9", required = false) Integer size,
                                                      @RequestParam(value = "all", required = false, defaultValue = "false") Boolean all) {
        ServiceResponse response = companyService.getAllCompany(page, size, all);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getOneCompany(@PathVariable("id") Integer id) {
        ServiceResponse response = companyService.getOneCompany(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @PostMapping("/business-license")
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> uploadBusinessLicense(@RequestParam("file") MultipartFile file) {
        ServiceResponse response = companyService.uploadBusinessLicense(file);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @DeleteMapping("/business-license")
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> deleteBusinessLicense() {
        ServiceResponse response = companyService.deleteBusinessLicense();
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @GetMapping("/business-license")
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> getBusinessLicense() {
        ServiceResponse response = companyService.getBusinessLicense();
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

}
