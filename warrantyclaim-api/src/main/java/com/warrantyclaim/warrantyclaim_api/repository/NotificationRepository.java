package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    // Lấy tất cả notifications của user
    Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    // Lấy notifications chưa đọc của user
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(String userId);

    // Đếm số notifications chưa đọc
    long countByUserIdAndIsReadFalse(String userId);

    // Lấy notifications theo type
    Page<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(String userId, String type, Pageable pageable);
}
