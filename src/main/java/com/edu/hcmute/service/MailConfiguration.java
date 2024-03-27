package com.edu.hcmute.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfiguration {
    @Bean
    public JavaMailSender javaMailSender() {
        // Configure and return your JavaMailSender instance here
        // (e.g., using properties from application.properties or application.yml)
        return new JavaMailSenderImpl();
    }
}
