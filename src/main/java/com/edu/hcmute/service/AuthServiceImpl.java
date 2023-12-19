package com.edu.hcmute.service;


import com.edu.hcmute.constant.Message;
import com.edu.hcmute.constant.Role;
import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.response.ResponseDataSatus;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.utils.VerificationCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AppUserRepository userRepository;
    private final EmailSender emailSender;
    private final RedisTemplate redisTemplate;
    @Override
    public ServiceResponse register(RegisterDTO registerDTO) {
        AppUser user = userRepository.findByEmail(registerDTO.getEmail().trim())
                .orElse(null);
        if (user != null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.EMAIL_ALREADY_EXISTS)
                    .build();
        }

        if (!registerDTO.isPasswordMatching()) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.PASSWORD_NOT_MATCHING)
                    .build();
        }

        String verifyCode = VerificationCodeUtils.generateSixDigitCode();
        redisTemplate.opsForValue().set(verifyCode + "_register", registerDTO, Duration.ofMinutes(10));

        log.info("verifyCode: {}", redisTemplate.opsForValue().get(verifyCode + "_register"));
        CompletableFuture.runAsync(() -> {
            emailSender.send(
                    registerDTO.getEmail(),
                    "Xác thực tài khoản",
                    "<h1>Mã xác thực của bạn là: " + verifyCode + "</h1>");
        });

        return ServiceResponse.builder()
                .statusCode(HttpStatus.CREATED)
                .status(ResponseDataSatus.SUCCESS)
                .message(Message.REGISTER_SUCCESS)
                .data(registerDTO)
                .build();
    }

    @Override
    public ServiceResponse verifyRegister(String token) {
        log.info("token: {}", token);
        RegisterDTO registerDTO = (RegisterDTO)redisTemplate.opsForValue().get(token + "_register");
        log.info("registerDTO: {}", redisTemplate.opsForValue().get(token + "_register"));
        if (registerDTO == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.INVALID_OTP)
                    .build();
        }

        AppUser user = AppUser.builder()
                .email(registerDTO.getEmail())
                .password(registerDTO.getPassword())
                .fullName(registerDTO.getFullName())
                .role(Role.valueOf(registerDTO.getRole()))
                .build();

        userRepository.save(user);

        redisTemplate.delete(token + "_register");

        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataSatus.SUCCESS)
                .message(Message.VERIFY_SUCCESS)
                .build();
    }


}
