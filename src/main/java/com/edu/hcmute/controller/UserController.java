package com.edu.hcmute.controller;


import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AppUserService appUserService;

    @GetMapping()
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/avatar")
    public ResponseEntity<ResponseData> changeUserAvatar(@RequestParam("file") MultipartFile multipartFile){
        ServiceResponse serviceResponse = appUserService.changeUserAvatar(multipartFile);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                .status(serviceResponse.getStatus())
                .message(serviceResponse.getMessage())
                .data(serviceResponse.getData())
                .build());
    }
}
