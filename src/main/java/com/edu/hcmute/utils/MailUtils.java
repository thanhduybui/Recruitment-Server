package com.edu.hcmute.utils;

import com.edu.hcmute.service.mail.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailUtils {

    private final EmailSender emailSender;
    private final RedisTemplate redisTemplate;

    public void generateCodeAndSendMail(String email) {
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
}
