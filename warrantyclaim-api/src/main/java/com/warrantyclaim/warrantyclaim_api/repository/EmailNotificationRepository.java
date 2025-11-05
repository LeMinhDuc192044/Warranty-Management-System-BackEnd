package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
    List<EmailNotification> findByClaimId(String claimId);
    List<EmailNotification> findBySuccessTrue();
}
