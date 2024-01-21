package com.edu.hcmute.controller;


import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/majors")
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @GetMapping
    public ResponseEntity<ResponseData> getAll(@RequestParam(value="all", required = false, defaultValue = "false") Boolean all) {
        ServiceResponse responseService = majorService.getAll(all);
        return ResponseEntity.status(responseService.getStatusCode())
                .body(ResponseData.builder()
                        .status(responseService.getStatus())
                        .message(responseService.getMessage())
                        .data(responseService.getData())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getOne(@PathVariable("id") Integer id) {
        ServiceResponse responseService = majorService.getOne(id);
        return ResponseEntity.status(responseService.getStatusCode())
                .body(ResponseData.builder()
                        .status(responseService.getStatus())
                        .message(responseService.getMessage())
                        .data(responseService.getData())
                        .build());
    }
}
