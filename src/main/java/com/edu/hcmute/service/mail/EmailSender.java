package com.edu.hcmute.service.mail;


import com.edu.hcmute.dto.ContentEmailDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmailSender {
    void sendVerifyCode(String to, String subject, ContentEmailDTO contentEmailDTO);
    void sendNotificationApplication(String to, String subject, ContentEmailDTO contentEmailDTO);
    void sendApplyResult(String to, String subject, ContentEmailDTO contentEmailDTO);
}
