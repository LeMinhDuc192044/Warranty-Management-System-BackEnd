package com.warrantyclaim.warrantyclaim_api.service;


import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtp(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mã OTP khôi phục mật khẩu");
        message.setText("Xin chào,\n\nMã OTP của bạn là: " + otp + "\nMã này có hiệu lực trong 5 phút.\n\nTrân trọng.");
        message.setFrom("your_email@gmail.com");
        mailSender.send(message);
    }


    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(fromEmail);
        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML

            mailSender.send(mimeMessage);
            log.info("HTML email sent successfully to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send HTML email to: {}", to, e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    public void sendHtmlEmailToMultiple(List<String> recipients, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(recipients.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            log.info("HTML email sent successfully to {} recipients", recipients.size());

        } catch (Exception e) {
            log.error("Failed to send HTML email to multiple recipients", e);
            throw new RuntimeException("Failed to send HTML email to multiple recipients", e);
        }
    }

    public void sendEmailWithCc(String to, List<String> cc, List<String> bcc,
                                String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);

            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc.toArray(new String[0]));
            }

            if (bcc != null && !bcc.isEmpty()) {
                helper.setBcc(bcc.toArray(new String[0]));
            }

            helper.setSubject(subject);
            helper.setText(body);

            mailSender.send(mimeMessage);
            log.info("Email sent with CC/BCC");

        } catch (Exception e) {
            log.error("Failed to send email with CC/BCC", e);
            throw new RuntimeException("Failed to send email with CC/BCC", e);
        }
    }
}

