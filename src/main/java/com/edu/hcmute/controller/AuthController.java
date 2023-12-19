package com.edu.hcmute.controller;

import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.dto.VerifyDTO;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseData> register(@RequestBody @Valid RegisterDTO registerDTO) {
        ServiceResponse responseService = authService.register(registerDTO);
        return ResponseEntity.status(responseService.getStatusCode()).body(ResponseData.builder()
                .status(responseService.getStatus())
                .message(responseService.getMessage())
                .build());
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseData> verify(@RequestBody VerifyDTO verifyDTO) {
        ServiceResponse responseService = authService.verifyRegister(verifyDTO.getOtp());
        return ResponseEntity.status(responseService.getStatusCode())
                .body(ResponseData.builder()
                        .status(responseService.getStatus())
                        .message(responseService.getMessage())
                        .data(responseService.getData())
                        .build());
    }
}
