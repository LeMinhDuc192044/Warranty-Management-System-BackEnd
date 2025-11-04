package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.EmailNotificationRequestDTO;
import com.warrantyclaim.warrantyclaim_api.dto.EmailNotificationResponseDTO;
import com.warrantyclaim.warrantyclaim_api.service.EmailNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/EmailNotifications")
@RequiredArgsConstructor
public class EmailNotificationController {

    private final EmailNotificationService emailNotificationService;

    // 1. Gửi email + lưu log
    @PostMapping("/notify-customer")
    public ResponseEntity<EmailNotificationResponseDTO> sendEmail(@Valid @RequestBody EmailNotificationRequestDTO dto) {
        EmailNotificationResponseDTO response = emailNotificationService.createEmailLog(dto);
        return ResponseEntity.ok(response);
    }

    // 2. Lấy danh sách email đã gửi
    @GetMapping
    public ResponseEntity<List<EmailNotificationResponseDTO>> getAllEmails() {
        List<EmailNotificationResponseDTO> emails = emailNotificationService.getAllEmailLogs();
        return ResponseEntity.ok(emails);
    }

    // 3. Lấy chi tiết email theo ID
    @GetMapping("/{id}")
    public ResponseEntity<EmailNotificationResponseDTO> getEmailById(@PathVariable Long id) {
        EmailNotificationResponseDTO email = emailNotificationService.getEmailLogById(id);
        return ResponseEntity.ok(email);
    }

    // 4. Cập nhật nội dung email
    @PutMapping("/{id}")
    public ResponseEntity<EmailNotificationResponseDTO> updateEmail(@PathVariable Long id,
                                                                    @RequestBody EmailNotificationRequestDTO dto) {
        EmailNotificationResponseDTO updated = emailNotificationService.updateEmailLog(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/all-email-sent")
    public ResponseEntity<List<EmailNotificationResponseDTO>> getAllSentEmails() {
        List<EmailNotificationResponseDTO> emails = emailNotificationService.getAllSentEmails();
        return ResponseEntity.ok(emails);
    }
}
