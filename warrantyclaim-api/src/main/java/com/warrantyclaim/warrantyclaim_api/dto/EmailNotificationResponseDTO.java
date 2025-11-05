package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailNotificationResponseDTO {
    private Long id;
    private String claimId;
    private String recipientEmail;
    private String subject;
    private String content;
    private LocalDateTime sentAt;
    private boolean success;
    private String errorMessage;
    private String statusLabel; // e.g. "Thành công" hoặc "Thất bại"

}
