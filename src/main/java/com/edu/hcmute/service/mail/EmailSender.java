package com.edu.hcmute.service.mail;


import org.springframework.stereotype.Service;

@Service
public interface EmailSender {
    void send(String to, String subject, String email);
}
