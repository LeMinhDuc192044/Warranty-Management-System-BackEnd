package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.NotificationDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyClaimNotificationRequest;
import com.warrantyclaim.warrantyclaim_api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * POST /api/notifications/warranty-claim - Gửi notification yêu cầu bảo hành
     * đến SC_ADMIN - Chỉ SC_STAFF và SC_ADMIN được gửi
     */
    @PostMapping("/warranty-claim")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN')")
    public ResponseEntity<Map<String, Object>> sendWarrantyClaimNotification(
            @RequestBody WarrantyClaimNotificationRequest request) {
        try {
            notificationService.sendWarrantyClaimNotificationToSCAdmins(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Notification sent to SC_ADMIN successfully");
            response.put("claimId", request.getClaimId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to send notification: " + e.getMessage());

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * GET /api/notifications/user/{userId} - Lấy tất cả notifications của user (có
     * phân trang) - User chỉ xem được của mình
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN', 'EVM_STAFF', 'EVM_ADMIN', 'SC_TECHNICIAN')")
    public ResponseEntity<Page<NotificationDTO>> getNotificationsByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId, pageable);
        return ResponseEntity.ok(notifications);
    }

    /**
     * GET /api/notifications/user/{userId}/unread - Lấy notifications chưa đọc
     * User chỉ xem được của mình
     */
    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN', 'EVM_STAFF', 'EVM_ADMIN', 'SC_TECHNICIAN')")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable String userId) {
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * GET /api/notifications/user/{userId}/unread/count - Đếm số notifications chưa
     * đọc - User chỉ xem được của mình
     */
    @GetMapping("/user/{userId}/unread/count")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN', 'EVM_STAFF', 'EVM_ADMIN', 'SC_TECHNICIAN')")
    public ResponseEntity<Long> countUnreadNotifications(@PathVariable String userId) {
        long count = notificationService.countUnreadNotifications(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * PATCH /api/notifications/{id}/read - Đánh dấu notification đã đọc
     * User chỉ đánh dấu được của mình
     */
    @PatchMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN', 'EVM_STAFF', 'EVM_ADMIN', 'SC_TECHNICIAN')")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable String id) {
        NotificationDTO notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    /**
     * PATCH /api/notifications/user/{userId}/read-all - Đánh dấu tất cả đã đọc
     * User chỉ đánh dấu được của mình
     */
    @PatchMapping("/user/{userId}/read-all")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN', 'EVM_STAFF', 'EVM_ADMIN', 'SC_TECHNICIAN')")
    public ResponseEntity<Void> markAllAsRead(@PathVariable String userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/notifications/{id} - Xóa notification
     * User chỉ xóa được của mình
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN', 'EVM_STAFF', 'EVM_ADMIN', 'SC_TECHNICIAN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
