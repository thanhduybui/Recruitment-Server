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
    public void sendVerifyCode(String to, String subject, ContentEmailDTO contentEmailDTO) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress("jobhuntadm@gmail.com"));
            helper.setTo(to);
            helper.setSubject(subject);

            String filePathAndName = "src\\main\\resources\\static\\email_template.html";

            String htmlTemplate = Arrays.toString(readFile(filePathAndName));
            htmlTemplate = htmlTemplate.replaceAll(", ","");
            htmlTemplate = htmlTemplate.replace("[","");
            htmlTemplate = htmlTemplate.replace("]","");

            System.out.print(htmlTemplate);

            if(htmlTemplate.contains("${code}")) {
                htmlTemplate = htmlTemplate.replace("${code}", contentEmailDTO.getCode());
            }
            if(htmlTemplate.contains("${company}")) {
                htmlTemplate = htmlTemplate.replace("${company}", contentEmailDTO.getCompanyName());
            }
            if(htmlTemplate.contains("${job}")) {
                htmlTemplate = htmlTemplate.replace("${job}", contentEmailDTO.getJobName());
            }
            if(htmlTemplate.contains("${candidate}")) {
                htmlTemplate = htmlTemplate.replace("${candidate}", contentEmailDTO.getName());
            }
            if(htmlTemplate.contains("${dateApply}")) {
                htmlTemplate = htmlTemplate.replace("${dateApply}", contentEmailDTO.getName());
            }

            helper.setText(htmlTemplate, true);

            mailSender.send(message);
            log.info("email sent");
        }catch (Exception e){
            log.error("failed to send email", e);
        }
    }

    @Override
    public void sendNotificationApplication(String to, String subject, ContentEmailDTO contentEmailDTO) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress("jobhuntadm@gmail.com"));
            helper.setTo(to);
            helper.setSubject(subject);

            String filePathAndName = "src\\main\\resources\\static\\email_job_application.html";

            String htmlTemplate = Arrays.toString(readFile(filePathAndName));
            htmlTemplate = htmlTemplate.replaceAll(", ","");
            htmlTemplate = htmlTemplate.replace("[","");
            htmlTemplate = htmlTemplate.replace("]","");

            System.out.print(htmlTemplate);

            if(htmlTemplate.contains("${code}")) {
                htmlTemplate = htmlTemplate.replace("${code}", contentEmailDTO.getCode());
            }
            if(htmlTemplate.contains("${company}")) {
                htmlTemplate = htmlTemplate.replace("${company}", contentEmailDTO.getCompanyName());
            }
            if(htmlTemplate.contains("${job}")) {
                htmlTemplate = htmlTemplate.replace("${job}", contentEmailDTO.getJobName());
            }
            if(htmlTemplate.contains("${candidate}")) {
                htmlTemplate = htmlTemplate.replace("${candidate}", contentEmailDTO.getName());
            }
            if(htmlTemplate.contains("${dateApply}")) {
                htmlTemplate = htmlTemplate.replace("${dateApply}", contentEmailDTO.getName());
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

    @Override
    public void sendApplyResult(String to, String subject, ContentEmailDTO contentEmailDTO) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress("jobhuntadm@gmail.com"));
            helper.setTo(to);
            helper.setSubject(subject);

            String filePathAndName = "src\\main\\resources\\static\\email_job_application_result.html";

            String htmlTemplate = Arrays.toString(readFile(filePathAndName));
            htmlTemplate = htmlTemplate.replaceAll(", ","");
            htmlTemplate = htmlTemplate.replace("[","");
            htmlTemplate = htmlTemplate.replace("]","");


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
            if(htmlTemplate.contains("${dateHandle}")) {
                htmlTemplate = htmlTemplate.replace("${dateHandle}", contentEmailDTO.getDateHandle());
            }

            helper.setText(htmlTemplate, true);

            mailSender.send(message);
            log.info("email sent");
        }catch (Exception e){
            log.error("failed to send email", e);
        }
    }
}
