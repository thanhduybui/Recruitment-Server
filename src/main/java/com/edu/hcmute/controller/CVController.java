package com.edu.hcmute.controller;


import com.edu.hcmute.dto.CvDTO;
import com.edu.hcmute.dto.CvRequestBody;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.CVService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cvs")
@RequiredArgsConstructor
public class CVController {

    private final CVService cvService;
    @PostMapping
    public ResponseEntity<ResponseData> createCV(@RequestBody @Valid CvRequestBody cvRequestBody) {
        ServiceResponse serviceResponse = cvService.createCV(cvRequestBody);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }
}
