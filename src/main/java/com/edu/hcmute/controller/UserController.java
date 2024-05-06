package com.edu.hcmute.controller;


import com.edu.hcmute.dto.*;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.user.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/search")
    public ResponseEntity<ResponseData> searchUser(@RequestParam("email") String email) {
       ServiceResponse serviceResponse = appUserService.searchUser(email);
         return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                          .status(serviceResponse.getStatus())
                          .message(serviceResponse.getMessage())
                          .data(serviceResponse.getData())
                          .build());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseData> getAllUserByRole(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                         @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                         @RequestParam(value = "role" , required = false) String role) {

        ServiceResponse serviceResponse = appUserService.getAllUserByRole(page, size, role);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @PostMapping
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
    public ResponseEntity<ResponseData> deleteUser(@PathVariable Long userId, @PathVariable String status) {

        ServiceResponse serviceResponse = appUserService.deleteUser(userId, status);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ResponseData> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        ServiceResponse serviceResponse = appUserService.resetPassword(resetPasswordDTO);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }


    @PutMapping("/cv-profile")
    public ResponseEntity<ResponseData> getUpdateProfileCV(@RequestBody @Valid FindJobProfileRequestBody findJobProfileRequestBody) {
        ServiceResponse serviceResponse = appUserService.updateUserCvProfile(findJobProfileRequestBody);
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }

    @GetMapping("/cv-profile")
    public ResponseEntity<ResponseData> getUpdateProfileCV() {
        ServiceResponse serviceResponse = appUserService.getUserCvProfile();
        return ResponseEntity.status(serviceResponse.getStatusCode())
                .body(ResponseData.builder()
                        .status(serviceResponse.getStatus())
                        .message(serviceResponse.getMessage())
                        .data(serviceResponse.getData())
                        .build());
    }
}
