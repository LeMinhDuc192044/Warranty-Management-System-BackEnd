package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.EmailNotificationRequestDTO;
import com.warrantyclaim.warrantyclaim_api.dto.EmailNotificationResponseDTO;

import java.util.List;

public interface EmailNotificationService {
    EmailNotificationResponseDTO createEmailLog(EmailNotificationRequestDTO dto);
    List<EmailNotificationResponseDTO> getAllEmailLogs();
    EmailNotificationResponseDTO getEmailLogById(Long id);
    EmailNotificationResponseDTO updateEmailLog(Long id, EmailNotificationRequestDTO dto);
    List<EmailNotificationResponseDTO> getAllSentEmails();
}
