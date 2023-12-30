package com.edu.hcmute.controller;

import com.edu.hcmute.dto.LoginDTO;
import com.edu.hcmute.dto.RecruiterRegisterDTO;
import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.dto.VerifyDTO;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.auth.GenericAuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final GenericAuthServiceImpl<RegisterDTO> candidateAuthService;
    private final GenericAuthServiceImpl<RecruiterRegisterDTO> recruiterAuthService;

    @PostMapping("/register")
    public ResponseEntity<ResponseData> register(@RequestBody @Valid RegisterDTO registerDTO) {
        ServiceResponse responseService = candidateAuthService.register(registerDTO);
        return ResponseEntity.status(responseService.getStatusCode()).body(ResponseData.builder()
                .status(responseService.getStatus())
                .message(responseService.getMessage())
                .build());
    }

    @PostMapping("/recruiter/register")
    public ResponseEntity<ResponseData> recruiterRegister(@RequestBody @Valid RecruiterRegisterDTO recruitRegisterDTO) {

        ServiceResponse responseService = recruiterAuthService.register(recruitRegisterDTO);
        return ResponseEntity.status(responseService.getStatusCode()).body(ResponseData.builder()
                .status(responseService.getStatus())
                .message(responseService.getMessage())
                .build());
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseData> verify(@RequestBody VerifyDTO verifyDTO) {
        ServiceResponse responseService = candidateAuthService.verifyRegister(verifyDTO);
        return ResponseEntity.status(responseService.getStatusCode())
                .body(ResponseData.builder()
                        .status(responseService.getStatus())
                        .message(responseService.getMessage())
                        .data(responseService.getData())
                        .build());
    }

    @GetMapping("/resend-verify-code")
    public ResponseEntity<ResponseData> resendVerifyCode(@RequestParam String email) {
        ServiceResponse responseService = candidateAuthService.resendVerifyCode(email);
        return ResponseEntity.status(responseService.getStatusCode())
                .body(ResponseData.builder()
                        .status(responseService.getStatus())
                        .message(responseService.getMessage())
                        .data(responseService.getData())
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseData> candidate(@RequestBody LoginDTO loginDTO) {
        ServiceResponse responseService = candidateAuthService.login(loginDTO);
        return ResponseEntity.status(responseService.getStatusCode())
                .body(ResponseData.builder()
                        .status(responseService.getStatus())
                        .message(responseService.getMessage())
                        .data(responseService.getData())
                        .build());
    }
}
