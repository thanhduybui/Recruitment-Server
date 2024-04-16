package com.edu.hcmute.controller;


import com.edu.hcmute.dto.OptionDTO;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.category.WorkModeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/work-modes")
@RequiredArgsConstructor
public class WorkModeController {
    private final WorkModeService workModeService;

    @GetMapping
    public ResponseEntity<ResponseData> getAllWorkMode(
            @RequestParam(value = "all", required = false, defaultValue = "false") Boolean all) {
        ServiceResponse response = workModeService.getAll(all);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getOneWorkMode(@PathVariable("id") Integer id) {
        ServiceResponse response = workModeService.getOne(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> createWorkMode(@RequestBody OptionDTO optionDTO) {
        ServiceResponse response = workModeService.create(optionDTO);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> updateWorkMode(@PathVariable("id") Integer id, @RequestBody OptionDTO optionDTO) {
        ServiceResponse response = workModeService.update(optionDTO, id);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('RECRUITER')")
    public ResponseEntity<ResponseData> deleteWorkMode(@PathVariable("id") Integer id) {
        ServiceResponse response = workModeService.delete(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

}
