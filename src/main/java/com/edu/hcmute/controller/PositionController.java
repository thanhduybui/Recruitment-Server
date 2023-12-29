package com.edu.hcmute.controller;


import com.edu.hcmute.dto.PositionDTO;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;
    @GetMapping
    public ResponseEntity<ResponseData> getAllPosition(){
        ServiceResponse res = positionService.getAll();
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getPositionById(@PathVariable("id") Integer Id){
        ServiceResponse res = positionService.getOne(Id);
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> createPosition(@RequestBody PositionDTO position){
        ServiceResponse res = positionService.create(position);
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }
}
