package com.edu.hcmute.service.mail;


import com.edu.hcmute.dto.ContentEmailDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmailSender {
    void send(String to, String subject, ContentEmailDTO contentEmailDTO);
}
