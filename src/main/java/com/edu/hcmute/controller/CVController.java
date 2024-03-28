package com.edu.hcmute.controller;


import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.CVService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cv")
public class CVController {
    private final CVService cvService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseData> uploadCV(@RequestParam("file") MultipartFile multipartFile, @RequestParam("name") String name) {
        ServiceResponse res = cvService.uploadCV(multipartFile, name);
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getCV(@PathVariable("id") Long id) {
        ServiceResponse res = cvService.getOneCV(id);
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseData> getCVByUser() {
        ServiceResponse res = cvService.getUserCv();
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }
}
