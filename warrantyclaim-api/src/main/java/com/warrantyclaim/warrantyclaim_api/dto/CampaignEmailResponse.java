package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignEmailResponse {

    private String campaignName;
    private Integer totalRecipients;
    private Integer successCount;
    private Integer failedCount;
    private List<String> successEmails;
    private List<String> failedEmails;
    private LocalDateTime sentAt;
    private String errorMessage;

    // Helper methods
    public boolean isFullySuccessful() {
        return failedCount == 0;
    }

    public double getSuccessRate() {
        if (totalRecipients == 0) return 0.0;
        return (successCount * 100.0) / totalRecipients;
    }
}