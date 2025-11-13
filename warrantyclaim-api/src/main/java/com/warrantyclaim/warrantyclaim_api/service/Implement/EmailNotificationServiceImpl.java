package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.EmailNotificationRequestDTO;
import com.warrantyclaim.warrantyclaim_api.dto.EmailNotificationResponseDTO;
import com.warrantyclaim.warrantyclaim_api.entity.EmailNotification;
import com.warrantyclaim.warrantyclaim_api.entity.WarrantyClaim;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.repository.EmailNotificationRepository;
import com.warrantyclaim.warrantyclaim_api.repository.WarrantyClaimRepository;
import com.warrantyclaim.warrantyclaim_api.service.EmailNotificationService;
import com.warrantyclaim.warrantyclaim_api.service.EmailService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final EmailNotificationRepository repository;
    private final EmailService emailService;
    private final WarrantyClaimRepository warrantyClaimRepository;

    @Override
    public EmailNotificationResponseDTO createEmailLog(EmailNotificationRequestDTO dto) {
        // Lấy thông tin claim từ claimId
        WarrantyClaim claim = warrantyClaimRepository.findById(dto.getClaimId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy claim với ID: " + dto.getClaimId()));

        // Kiểm tra trạng thái claim
        if (claim.getStatus() != WarrantyClaimStatus.COMPLETED) {
            throw new IllegalStateException("Xe chưa hoàn tất bảo hành.");
        }

        // Kiểm tra ngày nhận xe
        if (claim.getReturnDate() == null) {
            throw new IllegalStateException("Ngày nhận xe chưa được cập nhật.");
        }

        // Tạo nội dung email từ dữ liệu claim
        String to = claim.getEmail();
        String name = claim.getCustomerName();
        LocalDate returnDate = claim.getReturnDate();

        String subject = "Thông báo nhận xe sau bảo hành";
        String body = String.format(
                "Chào %s,\n\nXe của bạn đã hoàn tất bảo hành và sẵn sàng nhận vào ngày %s.\n\nTrân trọng,\nTrung tâm bảo hành",
                name, returnDate
        );

        // Gửi email và lưu log
        EmailNotification email = new EmailNotification();
        email.setClaimId(dto.getClaimId());
        email.setRecipientEmail(to);
        email.setSubject(subject);
        email.setContent(body);
        email.setSentAt(LocalDateTime.now());

        try {
            emailService.sendEmail(to, subject, body);
            email.setSuccess(true);
        } catch (Exception e) {
            email.setSuccess(false);
            email.setErrorMessage(e.getMessage());
        }

        repository.save(email);
        return toResponseDTO(email);
    }

    @Override
    public List<EmailNotificationResponseDTO> getAllEmailLogs() {
        return repository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public EmailNotificationResponseDTO getEmailLogById(Long id) {
        EmailNotification email = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email log not found with ID: " + id));
        return toResponseDTO(email);
    }

    @Override
    public EmailNotificationResponseDTO updateEmailLog(Long id, EmailNotificationRequestDTO dto) {
        EmailNotification email = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email log not found with ID: " + id));

        // Kiểm tra nếu email đã gửi thành công thì không cho phép cập nhật claimId
        if (email.isSuccess()) {
            throw new IllegalStateException("Không thể cập nhật claimId vì email đã được gửi thành công.");
        }

        // Cập nhật claimId nếu chưa gửi thành công
        email.setClaimId(dto.getClaimId());

        repository.save(email);
        return toResponseDTO(email);
    }

    private EmailNotificationResponseDTO toResponseDTO(EmailNotification email) {
        EmailNotificationResponseDTO dto = new EmailNotificationResponseDTO();
        dto.setId(email.getId());
        dto.setClaimId(email.getClaimId());
        dto.setRecipientEmail(email.getRecipientEmail());
        dto.setSubject(email.getSubject());
        dto.setContent(email.getContent());
        dto.setSentAt(email.getSentAt());
        dto.setSuccess(email.isSuccess());
        dto.setErrorMessage(email.getErrorMessage());
        dto.setStatusLabel(email.isSuccess() ? "Thành công" : "Thất bại");
        return dto;
    }

    @Override
    public List<EmailNotificationResponseDTO> getAllSentEmails() {
        return repository.findBySuccessTrue()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

}
