package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.OtpVerifyRequest;
import com.warrantyclaim.warrantyclaim_api.dto.ResetPasswordRequest;
import com.warrantyclaim.warrantyclaim_api.service.PasswordRecoverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
public class PasswordRecoverController {

    private final PasswordRecoverService recoverService;

    public PasswordRecoverController(PasswordRecoverService recoverService) {
        this.recoverService = recoverService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> sendOtp(@RequestParam String email) {
        recoverService.sendOtp(email);
        return ResponseEntity.ok(ApiResponse.success("OTP đã gửi qua email", null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@RequestBody OtpVerifyRequest req) {
        recoverService.verifyOtp(req.getEmail(), req.getOtp());
        return ResponseEntity.ok(ApiResponse.success("OTP hợp lệ", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody ResetPasswordRequest req) {
        recoverService.resetPassword(req.getEmail(), req.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success("Đặt lại mật khẩu thành công", null));
    }
}


