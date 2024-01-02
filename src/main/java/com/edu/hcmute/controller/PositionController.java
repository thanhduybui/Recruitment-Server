package com.edu.hcmute.controller;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.PositionService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ResponseData> getAllPosition(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all){
        ServiceResponse res = positionService.getAll(all);
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
    public ResponseEntity<ResponseData> createPosition(@RequestBody OptionDTO position){
        ServiceResponse res = positionService.create(position);
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> updatePosition(@PathVariable("id") Integer Id, @RequestBody @Valid OptionDTO position){
        ServiceResponse res = positionService.update(position, Id);
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deletePosition(@PathVariable("id") Integer Id){
        ServiceResponse res = positionService.delete(Id);
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }
}
