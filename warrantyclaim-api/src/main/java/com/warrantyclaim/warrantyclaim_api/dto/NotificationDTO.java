package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String id;
    private String userId;
    private String type; // WARRANTY_CLAIM, PARTS_REQUEST, RECALL, SERVICE_CAMPAIGN
    private String title;
    private String message;
    private String relatedEntityId;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}
