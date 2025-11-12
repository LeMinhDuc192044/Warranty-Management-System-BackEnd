package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.NotificationDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyClaimNotificationRequest;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

        /**
         * Tạo notification mới
         */
        NotificationDTO createNotification(NotificationDTO notificationDTO);

        /**
         * Tạo notification cho nhiều users (broadcast)
         */
        void createNotificationForUsers(List<String> userIds, String type, String title, String message,
                        String relatedEntityId);

        /**
         * Gửi notification yêu cầu bảo hành mới đến SC_ADMIN theo branchOffice
         */
        void sendWarrantyClaimNotificationToSCAdmins(WarrantyClaimNotificationRequest request);

        /**
         * Gửi notification khi SC_ADMIN duyệt claim đến TẤT CẢ SC_STAFF trong cùng
         * branch
         */
        void sendClaimApprovedNotificationToStaff(String claimId, String customerName, OfficeBranch branchOffice);

        /**
         * Gửi notification khi SC_ADMIN từ chối claim đến SC_STAFF tạo claim
         */
        void sendClaimRejectedNotificationToStaff(String claimId, String customerName, String rejectionReason,
                        Long staffUserId);

        /**
         * Gửi notification khi SC_STAFF assign claim đến SC_TECHNICIAN
         */
        void sendClaimAssignedNotificationToTechnician(String claimId, String customerName, String vehicleName,
                        Long technicianUserId);

        /**
         * Gửi notification khi SC_ADMIN tạo parts request mới đến EVM_STAFF
         */
        void sendPartsRequestCreatedNotificationToEVMStaff(String requestId, String scAdminName, String branchOffice,
                        String partName, Integer quantity);

        /**
         * Gửi notification khi EVM_STAFF approve parts request đến SC_ADMIN
         */
        void sendPartsRequestApprovedNotificationToSCAdmin(String requestId, String partName, String scAdminUserId);

        /**
         * Gửi notification khi EVM_STAFF reject parts request đến SC_ADMIN
         */
        void sendPartsRequestRejectedNotificationToSCAdmin(String requestId, String partName, String rejectionReason,
                        String scAdminUserId);

        /**
         * Lấy tất cả notifications của user (có phân trang)
         */
        Page<NotificationDTO> getNotificationsByUserId(String userId, Pageable pageable);

        /**
         * Lấy notifications chưa đọc của user
         */
        List<NotificationDTO> getUnreadNotifications(String userId);

        /**
         * Đếm số notifications chưa đọc
         */
        long countUnreadNotifications(String userId);

        /**
         * Đánh dấu notification đã đọc
         */
        NotificationDTO markAsRead(String notificationId);

        /**
         * Đánh dấu tất cả notifications của user đã đọc
         */
        void markAllAsRead(String userId);

        /**
         * Xóa notification
         */
        void deleteNotification(String notificationId);
}
