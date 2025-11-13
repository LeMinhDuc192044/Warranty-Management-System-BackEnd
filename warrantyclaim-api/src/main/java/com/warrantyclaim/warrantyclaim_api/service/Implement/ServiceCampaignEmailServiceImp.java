package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.CampaignEmailRequest;
import com.warrantyclaim.warrantyclaim_api.dto.CampaignEmailResponse;
import com.warrantyclaim.warrantyclaim_api.service.EmailService;
import com.warrantyclaim.warrantyclaim_api.service.ServiceCampaignEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceCampaignEmailServiceImp implements ServiceCampaignEmailService {

    private final EmailService emailService;

    @Override
    public CampaignEmailResponse sendCampaignEmail(CampaignEmailRequest request) {
        List<String> successEmails = new ArrayList<>();
        List<String> failedEmails = new ArrayList<>();

        // Send to all recipients
        for (String recipient : request.getRecipients()) {
            try {
                emailService.sendEmail(
                        recipient,
                        request.getSubject(),
                        request.getContent()
                );
                successEmails.add(recipient);
                log.info("Campaign email sent successfully to: {}", recipient);

            } catch (Exception e) {
                failedEmails.add(recipient);
                log.error("Failed to send campaign email to: {}", recipient, e);
            }
        }

        // Build response
        return CampaignEmailResponse.builder()
                .campaignName(request.getCampaignName())
                .totalRecipients(request.getRecipients().size())
                .successCount(successEmails.size())
                .failedCount(failedEmails.size())
                .successEmails(successEmails)
                .failedEmails(failedEmails)
                .sentAt(LocalDateTime.now())
                .build();
    }

    @Override
    public CampaignEmailResponse sendHtmlCampaignEmail(CampaignEmailRequest request) {
        List<String> successEmails = new ArrayList<>();
        List<String> failedEmails = new ArrayList<>();

        for (String recipient : request.getRecipients()) {
            try {
                emailService.sendHtmlEmail(
                        recipient,
                        request.getSubject(),
                        request.getContent() // HTML content
                );
                successEmails.add(recipient);
                log.info("HTML campaign email sent to: {}", recipient);

            } catch (Exception e) {
                failedEmails.add(recipient);
                log.error("Failed to send HTML campaign email to: {}", recipient, e);
            }
        }

        return CampaignEmailResponse.builder()
                .campaignName(request.getCampaignName())
                .totalRecipients(request.getRecipients().size())
                .successCount(successEmails.size())
                .failedCount(failedEmails.size())
                .successEmails(successEmails)
                .failedEmails(failedEmails)
                .sentAt(LocalDateTime.now())
                .build();
    }

    @Override
    public CampaignEmailResponse sendCampaignEmailBulk(CampaignEmailRequest request) {
        try {
            emailService.sendHtmlEmailToMultiple(
                    request.getRecipients(),
                    request.getSubject(),
                    request.getContent()
            );

            return CampaignEmailResponse.builder()
                    .campaignName(request.getCampaignName())
                    .totalRecipients(request.getRecipients().size())
                    .successCount(request.getRecipients().size())
                    .failedCount(0)
                    .successEmails(request.getRecipients())
                    .failedEmails(List.of())
                    .sentAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("Failed to send bulk campaign email", e);

            return CampaignEmailResponse.builder()
                    .campaignName(request.getCampaignName())
                    .totalRecipients(request.getRecipients().size())
                    .successCount(0)
                    .failedCount(request.getRecipients().size())
                    .successEmails(List.of())
                    .failedEmails(request.getRecipients())
                    .sentAt(LocalDateTime.now())
                    .errorMessage(e.getMessage())
                    .build();
        }

    }
}
