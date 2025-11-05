package com.warrantyclaim.warrantyclaim_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Email_Notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String claimId;
    private String recipientEmail;
    private String subject;

    @Column(length = 1000)
    private String content;

    private LocalDateTime sentAt;
    private boolean success;
    private String errorMessage;
}
