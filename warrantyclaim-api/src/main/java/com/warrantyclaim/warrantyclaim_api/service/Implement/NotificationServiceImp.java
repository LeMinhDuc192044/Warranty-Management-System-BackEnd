package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.NotificationDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyClaimNotificationRequest;
import com.warrantyclaim.warrantyclaim_api.entity.Notification;
import com.warrantyclaim.warrantyclaim_api.entity.User;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.Role;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.repository.NotificationRepository;
import com.warrantyclaim.warrantyclaim_api.repository.UserRepository;
import com.warrantyclaim.warrantyclaim_api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = toEntity(notificationDTO);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);

        Notification saved = notificationRepository.save(notification);
        return toDTO(saved);
    }

    @Override
    public void createNotificationForUsers(List<String> userIds, String type, String title, String message,
            String relatedEntityId) {
        for (String userId : userIds) {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setType(type);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setRelatedEntityId(relatedEntityId);
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }
    }

    @Override
    public void sendWarrantyClaimNotificationToSCAdmins(WarrantyClaimNotificationRequest request) {
        // Tìm tất cả SC_ADMIN trong cùng branchOffice
        List<User> scAdmins = userRepository.findByBranchOfficeAndRolesIn(
                request.getBranchOffice(),
                Set.of(Role.SC_ADMIN));

        if (scAdmins.isEmpty()) {
            return;
        }

        // Tạo notification cho từng SC_ADMIN
        String title = "Yêu cầu bảo hành mới";
        String message = String.format(
                "Yêu cầu bảo hành mới từ %s. Khách hàng: %s. Mã claim: %s. Vui lòng xem xét và duyệt.",
                request.getCreatedBy() != null ? request.getCreatedBy() : "SC_STAFF",
                request.getCustomerName(),
                request.getClaimId());

        for (User scAdmin : scAdmins) {
            Notification notification = new Notification();
            notification.setUserId(String.valueOf(scAdmin.getId()));
            notification.setType("WARRANTY_CLAIM");
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setRelatedEntityId(request.getClaimId());
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }
    }

    @Override
    public void sendClaimApprovedNotificationToStaff(String claimId, String customerName, OfficeBranch branchOffice) {
        // Tìm tất cả SC_STAFF trong cùng branch
        List<User> scStaffs = userRepository.findByBranchOfficeAndRolesIn(
                branchOffice.name(),
                Set.of(Role.SC_STAFF));

        if (scStaffs.isEmpty()) {
            return;
        }

        String title = "Yêu cầu bảo hành đã được duyệt";
        String message = String.format(
                "Yêu cầu bảo hành %s (Khách hàng: %s) đã được SC_ADMIN duyệt. Bạn có thể phân công kỹ thuật viên để xử lý.",
                claimId,
                customerName);

        // Tạo notification cho tất cả SC_STAFF trong branch
        for (User staff : scStaffs) {
            Notification notification = new Notification();
            notification.setUserId(String.valueOf(staff.getId()));
            notification.setType("WARRANTY_CLAIM_APPROVED");
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setRelatedEntityId(claimId);
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }
    }

    @Override
    public void sendClaimRejectedNotificationToStaff(String claimId, String customerName, String rejectionReason,
            Long staffUserId) {
        String title = "Yêu cầu bảo hành bị từ chối";
        String message = String.format(
                "Yêu cầu bảo hành %s (Khách hàng: %s) đã bị SC_ADMIN từ chối. Lý do: %s. Nhấn để xem chi tiết và bổ sung thông tin.",
                claimId,
                customerName,
                rejectionReason);

        Notification notification = new Notification();
        notification.setUserId(String.valueOf(staffUserId));
        notification.setType("WARRANTY_CLAIM_REJECTED");
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedEntityId(claimId);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Override
    public void sendClaimAssignedNotificationToTechnician(String claimId, String customerName, String vehicleName,
            Long technicianUserId) {
        String title = "🔧 Bạn được phân công xử lý yêu cầu bảo hành";
        String message = String.format(
                "Yêu cầu bảo hành %s cho xe %s của khách hàng %s đã được phân công cho bạn. Nhấn để xem chi tiết và bắt đầu công việc.",
                claimId,
                vehicleName,
                customerName);

        Notification notification = new Notification();
        notification.setUserId(String.valueOf(technicianUserId));
        notification.setType("CLAIM_ASSIGNED");
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedEntityId(claimId);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Override
    public void sendPartsRequestCreatedNotificationToEVMStaff(String requestId, String scAdminName,
            String branchOffice, String partName, Integer quantity) {
        // Tìm tất cả EVM_STAFF và EVM_ADMIN
        List<User> evmStaffs = userRepository.findByRolesIn(Set.of(Role.EVM_STAFF, Role.EVM_ADMIN));

        if (evmStaffs.isEmpty()) {
            return;
        }

        // Tạo notification cho từng EVM_STAFF
        String title = "Yêu cầu phụ tùng mới";
        String message = String.format(
                "Yêu cầu phụ tùng mới từ %s (%s). Phụ tùng: %s (SL: %d). Mã yêu cầu: %s. Vui lòng xem xét và phê duyệt.",
                scAdminName,
                branchOffice,
                partName,
                quantity,
                requestId);

        for (User evmStaff : evmStaffs) {
            Notification notification = new Notification();
            notification.setUserId(String.valueOf(evmStaff.getId()));
            notification.setType("PARTS_REQUEST");
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setRelatedEntityId(requestId);
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }
    }

    @Override
    public void sendPartsRequestApprovedNotificationToSCAdmin(String requestId, String partName, String scAdminUserId) {
        String title = "Yêu cầu phụ tùng đã được chấp nhận";
        String message = String.format(
                "Yêu cầu phụ tùng %s (Phụ tùng: %s) đã được EVM_STAFF chấp nhận. Phụ tùng sẽ sớm được giao đến trung tâm của bạn.",
                requestId,
                partName);

        Notification notification = new Notification();
        notification.setUserId(scAdminUserId);
        notification.setType("PARTS_REQUEST_APPROVED");
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedEntityId(requestId);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Override
    public void sendPartsRequestRejectedNotificationToSCAdmin(String requestId, String partName,
            String rejectionReason, String scAdminUserId) {
        String title = "Yêu cầu phụ tùng bị từ chối";
        String message = String.format(
                "Yêu cầu phụ tùng %s (Phụ tùng: %s) đã bị EVM_STAFF từ chối. Lý do: %s. Vui lòng xem xét và tạo yêu cầu mới nếu cần.",
                requestId,
                partName,
                rejectionReason != null ? rejectionReason : "Không có lý do cụ thể");

        Notification notification = new Notification();
        notification.setUserId(scAdminUserId);
        notification.setType("PARTS_REQUEST_REJECTED");
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedEntityId(requestId);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> getNotificationsByUserId(String userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return notifications.map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotifications(String userId) {
        List<Notification> notifications = notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        return notifications.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadNotifications(String userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    public NotificationDTO markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());

        Notification updated = notificationRepository.save(notification);
        return toDTO(updated);
    }

    @Override
    public void markAllAsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);

        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
        }

        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public void deleteNotification(String notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException("Notification not found with ID: " + notificationId);
        }
        notificationRepository.deleteById(notificationId);
    }

    // Helper methods
    private NotificationDTO toDTO(Notification entity) {
        if (entity == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setType(entity.getType());
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setRelatedEntityId(entity.getRelatedEntityId());
        dto.setIsRead(entity.getIsRead());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setReadAt(entity.getReadAt());

        return dto;
    }

    private Notification toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        Notification entity = new Notification();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setType(dto.getType());
        entity.setTitle(dto.getTitle());
        entity.setMessage(dto.getMessage());
        entity.setRelatedEntityId(dto.getRelatedEntityId());
        entity.setIsRead(dto.getIsRead() != null ? dto.getIsRead() : false);
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        entity.setReadAt(dto.getReadAt());

        return entity;
    }
}
