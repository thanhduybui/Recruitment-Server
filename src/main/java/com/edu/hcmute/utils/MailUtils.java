package com.edu.hcmute.utils;

import com.edu.hcmute.controller.JobApplicationController;
import com.edu.hcmute.dto.ContentEmailDTO;
import com.edu.hcmute.dto.JobApplicationRequestBody;
import com.edu.hcmute.entity.JobApplication;
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
        contentEmailDTO.setCode(verifyCode);

        log.info("verifyCode: {}", redisTemplate.opsForValue().get(email + "_otp"));
        CompletableFuture.runAsync(() -> {
            emailSender.sendVerifyCode(
                    email,
                    "Xác thực tài khoản",
                    contentEmailDTO);
        });
    }

    public void sendMailJobApplycation(JobApplication jobApplication) {
        ContentEmailDTO contentEmailDTO1 = new ContentEmailDTO();
        contentEmailDTO1.setJobName(jobApplication.getJob().getTitle());
        contentEmailDTO1.setStatus(jobApplication.getStatus().toString());
        contentEmailDTO1.setName(jobApplication.getName());
        contentEmailDTO1.setCompanyName(jobApplication.getJob().getCompany().getName());
        contentEmailDTO1.setDateApply(jobApplication.getCreatedAt().toString());

        log.info("email: {}" , jobApplication.getEmail());

        CompletableFuture.runAsync(() -> {
            emailSender.sendNotificationApplication(
                    jobApplication.getEmail(),
                    "Thông báo nộp đơn ứng tuyển",
                    contentEmailDTO1);
        });
    }

    public void sendMailJobApplycationResult(JobApplication jobApplication) {
        ContentEmailDTO contentEmailDTO2 = new ContentEmailDTO();
        contentEmailDTO2.setJobName(jobApplication.getJob().getTitle());
        contentEmailDTO2.setStatus(jobApplication.getStatus().toString());
        contentEmailDTO2.setName(jobApplication.getName());
        contentEmailDTO2.setCompanyName(jobApplication.getJob().getCompany().getName());
        contentEmailDTO2.setDateApply(jobApplication.getCreatedAt().toString());
        contentEmailDTO2.setDateHandle(jobApplication.getUpdatedAt().toString());

        log.info("email: {}" , jobApplication.getEmail());

        CompletableFuture.runAsync(() -> {
            emailSender.sendApplyResult(
                    jobApplication.getEmail(),
                    "Thông báo kết quả ứng tuyển",
                    contentEmailDTO2);
        });
    }
}
