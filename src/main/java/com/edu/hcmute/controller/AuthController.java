package com.edu.hcmute.controller;

import com.edu.hcmute.dto.*;
import com.edu.hcmute.response.ResponseData;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.service.auth.GenericAuthServiceImpl;
import com.google.auth.oauth2.OAuth2Credentials;
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

    @PostMapping("/forget-password/send-code")
    public ResponseEntity<ResponseData> sendCode(@RequestBody @Valid ForgetPasswordDTO forgetPasswordDTO) {
        ServiceResponse responseService = candidateAuthService.forgetPassword(forgetPasswordDTO);
        return ResponseEntity.status(responseService.getStatusCode())
                .body(ResponseData.builder()
                        .status(responseService.getStatus())
                        .message(responseService.getMessage())
                        .data(responseService.getData())
                        .build());
    }
    @PostMapping("/forget-password/verify-code")
    public ResponseEntity<ResponseData> sendCode(@RequestBody @Valid VerifyDTO verifyDTO) {
        ServiceResponse responseService = candidateAuthService.verifyForgetPassword(verifyDTO);
        return ResponseEntity.status(responseService.getStatusCode())
                .body(ResponseData.builder()
                        .status(responseService.getStatus())
                        .message(responseService.getMessage())
                        .data(responseService.getData())
                        .build());
    }

    @PostMapping("/forget-password/create-password")
    public ResponseEntity<ResponseData> sendCode(@RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) {
        ServiceResponse responseService = candidateAuthService.updatePassword(updatePasswordDTO);
        return ResponseEntity.status(responseService.getStatusCode())
                .body(ResponseData.builder()
                        .status(responseService.getStatus())
                        .message(responseService.getMessage())
                        .data(responseService.getData())
                        .build());
    }

    @PostMapping("/google-oauth/login")
    public ResponseEntity<ResponseData> loginByGoogle(@RequestBody @Valid TokenDTO tokenDTO) {
        ServiceResponse responseService = candidateAuthService.googleAuth(tokenDTO);

        return ResponseEntity.status(responseService.getStatusCode())
                .body(ResponseData.builder()
                        .status(responseService.getStatus())
                        .message(responseService.getMessage())
                        .data(responseService.getData())
                        .build());
    }
}
