package com.warrantyclaim.warrantyclaim_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "NotificationID", length = 50)
    private String id;

    @Column(name = "UserID", length = 50, nullable = false)
    private String userId; // ID của user nhận notification

    @Column(name = "Type", length = 50, nullable = false)
    private String type; // WARRANTY_CLAIM, PARTS_REQUEST, RECALL, SERVICE_CAMPAIGN

    @Column(name = "Title", length = 200, nullable = false)
    private String title;

    @Column(name = "Message", length = 500)
    private String message;

    @Column(name = "RelatedEntityID", length = 50)
    private String relatedEntityId; // ID của warranty claim, parts request, etc.

    @Column(name = "IsRead", nullable = false)
    private Boolean isRead = false;

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "ReadAt")
    private LocalDateTime readAt;
}
