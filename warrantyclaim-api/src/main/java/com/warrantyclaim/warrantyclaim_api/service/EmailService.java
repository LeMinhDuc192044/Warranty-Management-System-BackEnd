package com.warrantyclaim.warrantyclaim_api.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
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


}

