package com.edu.hcmute.controller;


import com.edu.hcmute.dto.ProfileDTO;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.user.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final AppUserService appUserService;

    @PutMapping("/avatar")
    public ResponseEntity<ResponseData> changeUserAvatar(@RequestParam("file") MultipartFile multipartFile) {
        ServiceResponse serviceResponse = appUserService.changeUserAvatar(multipartFile);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @PutMapping("/profile")
    public ResponseEntity<ResponseData> updateUserProfile(@RequestBody @Valid ProfileDTO profileDTO) {
        ServiceResponse serviceResponse = appUserService.updateUserProfile(profileDTO);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseData> getUserProfile() {
        ServiceResponse serviceResponse = appUserService.getUserProfile();
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @GetMapping("/{id}/cv")
    public ResponseEntity<ResponseData> getAllCVOfUser(@PathVariable("id") Long id) {
        ServiceResponse serviceResponse = appUserService.getAllCVOfUser(id);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData> searchUser(@RequestParam("keyword") String keyword) {
       return null;
    }
}
