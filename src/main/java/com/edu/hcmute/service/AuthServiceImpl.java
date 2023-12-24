package com.edu.hcmute.service;


import com.edu.hcmute.constant.Message;
import com.edu.hcmute.constant.Role;
import com.edu.hcmute.dto.LoginDTO;
import com.edu.hcmute.dto.RegisterDTO;
import com.edu.hcmute.dto.VerifyDTO;
import com.edu.hcmute.entity.AppUser;
import com.edu.hcmute.entity.Canditdate;
import com.edu.hcmute.repository.AppUserRepository;
import com.edu.hcmute.repository.CandidateRepository;
import com.edu.hcmute.response.ResponseDataSatus;
import com.edu.hcmute.response.ServiceResponse;
import com.edu.hcmute.utils.BcryptUtils;
import com.edu.hcmute.utils.JwtUtils;
import com.edu.hcmute.utils.VerificationCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AppUserRepository userRepository;
    private final EmailSender emailSender;
    private final RedisTemplate redisTemplate;
    private final CandidateRepository candidateRepository;

    @Override
    public ServiceResponse register(RegisterDTO registerDTO) {

        Canditdate canditdate = candidateRepository.findByEmail(registerDTO.getEmail().trim())
                .orElse(null);


        if (canditdate != null) {
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


        // save data in redis in form of "email": "verifyCode" and "email": {...registerDTO}
        redisTemplate.opsForValue().set(registerDTO.getEmail(), registerDTO, Duration.ofHours(8));
        generateCodeAndSendMail(registerDTO.getEmail());


        return ServiceResponse.builder()
                .statusCode(HttpStatus.CREATED)
                .status(ResponseDataSatus.SUCCESS)
                .message(Message.REGISTER_SUCCESS)
                .data(registerDTO)
                .build();
    }

    @Override
    public ServiceResponse verifyRegister(VerifyDTO verifyDTO) {
        RegisterDTO registerDTO = (RegisterDTO) redisTemplate.opsForValue().get(verifyDTO.getEmail());
        String verifyCode = (String) redisTemplate.opsForValue().get(verifyDTO.getEmail() + "_otp");

        if (registerDTO == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.EXPIRED_VERIFICATION_TIME)
                    .build();
        }

        if (verifyCode == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.EXPIRED_OTP)
                    .build();
        }

        if (!verifyDTO.getOtp().equals(verifyCode)) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.INVALID_OTP)
                    .build();
        }

        AppUser user = AppUser.builder()
                .email(registerDTO.getEmail())
                .password(BcryptUtils.hashPassword(registerDTO.getPassword()))
                .fullName(registerDTO.getFullName())
                .role(Role.valueOf(registerDTO.getRole()))
                .build();

        // save user into database
        userRepository.save(user);

        // delete otp save in redis
        redisTemplate.delete(verifyDTO.getEmail() + "_otp");
        //delete registerDTO save in redis
        redisTemplate.delete(verifyDTO.getEmail());

        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataSatus.SUCCESS)
                .message(Message.VERIFY_SUCCESS)
                .build();
    }

    @Override
    public ServiceResponse resendVerifyCode(String email) {
        RegisterDTO registerDTO = (RegisterDTO) redisTemplate.opsForValue().get(email);
        // check if registerDTO is expired
        if (registerDTO == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.EXPIRED_VERIFICATION_TIME)
                    .build();
        }
        // delete old otp
        redisTemplate.delete(email + "_otp");
        generateCodeAndSendMail(email);
        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataSatus.SUCCESS)
                .message(Message.RESEND_OTP_SUCCESS)
                .build();
    }

    private void generateCodeAndSendMail(String email) {
        String verifyCode = VerificationCodeUtils.generateSixDigitCode();
        redisTemplate.opsForValue().set(email + "_otp", verifyCode, Duration.ofMinutes(5));

        log.info("verifyCode: {}", redisTemplate.opsForValue().get(email + "_otp"));
        CompletableFuture.runAsync(() -> {
            emailSender.send(
                    email,
                    "Xác thực tài khoản",
                    "<h1>Mã xác thực của bạn là: " + verifyCode + "</h1>");
        });
    }


    @Override
    public ServiceResponse login(LoginDTO loginDTO) {
        AppUser user = userRepository.findByEmail(loginDTO.getEmail())
                .orElse(null);
        if (user == null) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.ACCOUNT_NOT_FOUND)
                    .build();
        }

        if (!BcryptUtils.verifyPassword(loginDTO.getPassword(), user.getPassword())) {
            return ServiceResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .status(ResponseDataSatus.ERROR)
                    .message(Message.PASSWORD_NOT_MATCHING)
                    .build();
        }

        return ServiceResponse.builder()
                .statusCode(HttpStatus.OK)
                .status(ResponseDataSatus.SUCCESS)
                .message(Message.LOGIN_SUCCESS)
                .data(Map.of("access_token", JwtUtils.generateToken(user.getEmail())))
                .build();
    }

}
