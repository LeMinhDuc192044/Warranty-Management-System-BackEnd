package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.entity.User;
import com.warrantyclaim.warrantyclaim_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordRecoverService {

    private final EmailService emailService;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    // Lưu OTP tạm thời: email → OTP + thời hạn
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    public PasswordRecoverService(EmailService emailService, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Gửi OTP đến email người dùng
    public void sendOtp(String email) {
        if (!userRepo.existsByEmail(email)) {
            throw new RuntimeException("Email không tồn tại trong hệ thống.");
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // Tạo mã OTP 6 chữ số
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5); // Hết hạn sau 5 phút

        otpStore.put(email, new OtpData(otp, expiry));
        emailService.sendOtp(email, otp);

        System.out.println("OTP gửi đến " + email + ": " + otp); // Debug
    }

    // Xác thực OTP
    public void verifyOtp(String email, String otp) {
        validateOtp(email, otp);
    }

    // Đặt lại mật khẩu sau khi xác thực OTP
    public void resetPassword(String email, String newPassword) {
        OtpData data = otpStore.get(email);
        if (data == null || data.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP đã hết hạn hoặc chưa xác thực.");
        }

        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        otpStore.remove(email); // Xoá OTP sau khi dùng
    }

    // Hàm kiểm tra OTP
    private void validateOtp(String email, String otp) {
        OtpData data = otpStore.get(email);
        if (data == null) {
            throw new RuntimeException("Không tìm thấy OTP cho email này.");
        }
        if (!data.getCode().equals(otp)) {
            throw new RuntimeException("OTP không đúng.");
        }
        if (data.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP đã hết hạn.");
        }
    }

    // Class nội bộ lưu OTP và thời hạn
    private static class OtpData {
        private final String code;
        private final LocalDateTime expiry;

        public OtpData(String code, LocalDateTime expiry) {
            this.code = code;
            this.expiry = expiry;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getExpiry() {
            return expiry;
        }
    }
}
