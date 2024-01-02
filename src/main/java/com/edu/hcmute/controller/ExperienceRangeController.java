package com.edu.hcmute.controller;


import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.ExperienceRangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/experience-ranges")
public class ExperienceRangeController {
    private final ExperienceRangeService experienceRangeService;

    @GetMapping
    public ResponseEntity<ResponseData> getAllExperienceRange(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all){
        ServiceResponse response = experienceRangeService.getAll(all);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getOneExperienceRange(@PathVariable("id") Integer id){
        ServiceResponse response = experienceRangeService.getOne(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteExperienceRange(@PathVariable("id") Integer id){
        ServiceResponse response = experienceRangeService.delete(id);
        return ResponseEntity.status(response.getStatusCode())
                .body(ResponseData.builder()
                        .status(response.getStatus())
                        .message(response.getMessage())
                        .data(response.getData())
                        .build());
    }
}
