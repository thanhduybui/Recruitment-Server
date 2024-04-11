package com.edu.hcmute.controller;


import com.edu.hcmute.constant.Status;
import com.edu.hcmute.dto.AccountDTO;
import com.edu.hcmute.dto.LoginDTO;
import com.edu.hcmute.dto.ProfileDTO;
import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.user.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Put;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Stack;

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

    @GetMapping("/search")
    public ResponseEntity<ResponseData> searchUser(@RequestParam("keyword") String keyword) {
       return null;
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseData> getAllUserByRole(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                         @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                         @RequestParam(value = "role" , required = true) String role)
    {
        return null;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseData> createUser(@RequestBody @Valid RegisterDTO registerDTO) {

        ServiceResponse serviceResponse = appUserService.createUser(registerDTO);

        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @PutMapping("/change-info-login")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseData> changeInfoLogin(@RequestBody @Valid AccountDTO accountDTO) {

        ServiceResponse serviceResponse = appUserService.changeInfoUser(accountDTO);

        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @PutMapping("/delete-user/{userId}/{status}")
    public ResponseEntity<ResponseData> deleteUser(@PathVariable Long userId, @PathVariable Status status) {

        ServiceResponse serviceResponse = appUserService.deleteUser(userId, status);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }
}
