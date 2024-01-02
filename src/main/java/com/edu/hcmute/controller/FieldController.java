package com.edu.hcmute.controller;


import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fields")
public class FieldController {
    private final FieldService fieldService;


    @GetMapping
    public ResponseEntity<ResponseData> getAllField(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all){
        ServiceResponse res = fieldService.getAll(all);
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getOneField(@PathVariable("id") Integer id){
        ServiceResponse res = fieldService.getOne(id);
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteField(@PathVariable("id") Integer id){
        ServiceResponse res = fieldService.delete(id);
        return ResponseEntity.status(res.getStatusCode())
                .body(ResponseData.builder()
                        .status(res.getStatus())
                        .message(res.getMessage())
                        .data(res.getData())
                        .build());
    }
}