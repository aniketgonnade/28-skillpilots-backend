package com.skilladmin.serviceImpl;

import com.skilladmin.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableAsync
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Async
    @Override
    public void sendVerificationEmail(String toEmail, String subject, String content) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String emailContent = content ;

            helper.setText(emailContent, true);
            log.info("Email Send to {}", toEmail);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
