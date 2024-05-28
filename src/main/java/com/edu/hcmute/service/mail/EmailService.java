package com.edu.hcmute.service.mail;

import com.edu.hcmute.dto.ContentEmailDTO;
import com.edu.hcmute.service.mail.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static org.antlr.v4.runtime.misc.Utils.readFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements EmailSender {

    @Autowired
    private final JavaMailSender mailSender;

    @Async
    @Override
    public void send(String to, String subject, ContentEmailDTO contentEmailDTO) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress("jobhuntadm@gmail.com"));
            helper.setTo(to);
            helper.setSubject(subject);

            String filePathAndName = "src\\main\\resources\\statics\\email_template.html";
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource(".")).getFile() + filePathAndName);

            String htmlTemplate = Arrays.toString(readFile(filePathAndName));
            htmlTemplate = htmlTemplate.replaceAll(", ","");

            System.out.print(htmlTemplate);

            if(htmlTemplate.contains("${name}")) {
                htmlTemplate = htmlTemplate.replace("${name}", contentEmailDTO.getName());
            }
            if(htmlTemplate.contains("${title}")) {
                htmlTemplate = htmlTemplate.replace("${title}", contentEmailDTO.getTitle());
            }
            if(htmlTemplate.contains("${jobName}")) {
                htmlTemplate = htmlTemplate.replace("${jobName}", contentEmailDTO.getJobName());
            }
            if(htmlTemplate.contains("${companyName}")) {
                htmlTemplate = htmlTemplate.replace("${companyName}", contentEmailDTO.getCompanyName());
            }
            if(htmlTemplate.contains("${dateApply}")) {
                htmlTemplate = htmlTemplate.replace("${dateApply}", contentEmailDTO.getDateApply());
            }
            if(htmlTemplate.contains("${status}")) {
                htmlTemplate = htmlTemplate.replace("${status}", contentEmailDTO.getStatus());
            }

            helper.setText(htmlTemplate, true);

            mailSender.send(message);
            log.info("email sent");
        }catch (Exception e){
            log.error("failed to send email", e);
        }
    }
}
