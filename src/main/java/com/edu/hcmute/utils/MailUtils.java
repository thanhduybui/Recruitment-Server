package com.edu.hcmute.utils;

import com.edu.hcmute.dto.ContentEmailDTO;
import com.edu.hcmute.service.mail.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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

        ContentEmailDTO contentEmailDTO = new ContentEmailDTO();
        contentEmailDTO.setTitle("Xác thực tài khoản");
        contentEmailDTO.setStatus(verifyCode);

        log.info("verifyCode: {}", redisTemplate.opsForValue().get(email + "_otp"));
        CompletableFuture.runAsync(() -> {
            emailSender.send(
                    email,
                    "Xác thực tài khoản",
                    contentEmailDTO);
        });
    }
}
