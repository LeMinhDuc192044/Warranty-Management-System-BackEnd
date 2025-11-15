package com.warrantyclaim.warrantyclaim_api.service.Implement;
import java.time.LocalDateTime;
import java.util.Collections;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicle;
import com.warrantyclaim.warrantyclaim_api.entity.SCTechnician;
import com.warrantyclaim.warrantyclaim_api.entity.User;
import com.warrantyclaim.warrantyclaim_api.entity.WarrantyClaim;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.mapper.WarrantyClaimMapper;
import com.warrantyclaim.warrantyclaim_api.repository.ElectricVehicleRepository;
import com.warrantyclaim.warrantyclaim_api.repository.SCTechnicianRepository;
import com.warrantyclaim.warrantyclaim_api.repository.UserRepository;
import com.warrantyclaim.warrantyclaim_api.repository.WarrantyClaimRepository;
import com.warrantyclaim.warrantyclaim_api.service.EmailService;
import com.warrantyclaim.warrantyclaim_api.service.NotificationService;
import com.warrantyclaim.warrantyclaim_api.service.WarrantyClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarrantyClaimServiceImp implements WarrantyClaimService {

    private final WarrantyClaimRepository warrantyClaimRepository;
    private final ElectricVehicleRepository electricVehicleRepository;
    private final UserRepository userRepository;
    private final SCTechnicianRepository scTechnicianRepository;
    private final WarrantyClaimMapper warrantyClaimMapper;
    private final NotificationService notificationService;
    private final EmailService emailService;

    @Override
    @Transactional
    public WarrantyClaimResponseDTO createWarrantyClaim(WarrantyClaimCreateRequestDTO warrantyClaimRequest) {

        ElectricVehicle electricVehicle = electricVehicleRepository.findById(warrantyClaimRequest.getVehicleId()) // for
                .orElseThrow(() -> new ResourceNotFoundException("There is no Electric Vehicle with this ID!!!"));


        // Validate staff if provided
        // ScStaff staff = null;
        // if (warrantyClaimRequest.getScStaffId() != null &&
        // !warrantyClaimRequest.getScStaffId().isEmpty()) {
        // staff = scStaffRepository.findById(warrantyClaimRequest.getScStaffId())
        // .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: "
        // + warrantyClaimRequest.getScStaffId()));
        // }
        WarrantyClaim warrantyClaim = warrantyClaimMapper.toEntityWarrantyClaim(warrantyClaimRequest);
        warrantyClaim.setId(generateClaimId());
        warrantyClaim.setVehicle(electricVehicle);
        warrantyClaim.setStatus(WarrantyClaimStatus.PENDING);

        // Lưu thông tin user tạo claim để gửi notification sau này
        if (warrantyClaimRequest.getCreatedByUserId() != null) {
            warrantyClaim.setCreatedByUserId(warrantyClaimRequest.getCreatedByUserId());
        }

        warrantyClaim.getVehicle().setStatus(VehicleStatus.IN_WARRANTY);

        warrantyClaimRepository.save(warrantyClaim);

        return warrantyClaimMapper.toResponseWarrantyClaim(warrantyClaim);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarrantyClaimListResponseDTO> getAllClaims(Pageable pageable) {
        Page<WarrantyClaim> claims = warrantyClaimRepository.findAllWithVehicle(pageable);
        return claims.map(warrantyClaimMapper::toListResponse);
    }

    @Override
    public WarrantyClaimResponseDTO updateClaim(String claimId, WarrantyClaimUpdateRequestDTO request) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found with ID: " + claimId));

        if (request.getElectricVehicleId() != null) {
            ElectricVehicle electricVehicle = electricVehicleRepository.findById(request.getElectricVehicleId()) // for
                    // testing
                    // exception
                    .orElseThrow(() -> new ResourceNotFoundException("There is no Electric Vehicle with this ID!!!"));

            claim.setVehicle(electricVehicle);
        }

        // Update staff if provided
        // if (request.getScStaffId() != null && !request.getScStaffId().isEmpty()) {
        // SCStaff staff = scStaffRepository.findById(request.getScStaffId())
        // .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: "
        // + request.getScStaffId()));
        // claim.setScStaff(staff);
        // }

        // Update other fields
        warrantyClaimMapper.updateEntity(claim, request);
        WarrantyClaim updatedClaim = warrantyClaimRepository.save(claim);

        return warrantyClaimMapper.toResponseWarrantyClaim(updatedClaim);
    }

    private String generateClaimId() {
        return "WC-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public WarrantyClaimDetailResponseDTO getClaimById(String claimId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found with ID: " + claimId));

        return warrantyClaimMapper.toDetailResponse(claim);
    }

    @Override
    public WarrantyClaimResponseDTO updateClaimStatus(String claimId, WarrantyClaimStatus status) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found with ID: " + claimId));

        // Thêm đoạn kiểm tra ngay sau khi lấy claim
        if (status == WarrantyClaimStatus.COMPLETED && claim.getReturnDate() == null) {
            throw new IllegalStateException("Không thể hoàn thành khi chưa có ngày giao xe.");
        }

        claim.setStatus(status);
        WarrantyClaim updatedClaim = warrantyClaimRepository.save(claim);

        return warrantyClaimMapper.toResponseWarrantyClaim(updatedClaim);
    }

    @Override
    @Transactional
    public WarrantyClaimResponseDTO approveOrRejectClaim(ApproveRejectClaimRequest request) {
        WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Warranty claim not found with ID: " + request.getClaimId()));

        // Kiểm tra claim phải ở trạng thái PENDING
        if (claim.getStatus() != WarrantyClaimStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING claims can be approved or rejected. Current status: " + claim.getStatus());
        }

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            // Duyệt claim
            claim.setStatus(WarrantyClaimStatus.APPROVED);
            claim.setRejectionReason(null); // Clear rejection reason if any

            // Gửi notification cho TẤT CẢ SC_STAFF trong cùng branch
            if (claim.getOfficeBranch() != null) {
                notificationService.sendClaimApprovedNotificationToStaff(
                        claim.getId(),
                        claim.getCustomerName(),
                        claim.getOfficeBranch());
            }

        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {
            // Từ chối claim
            if (request.getRejectionReason() == null || request.getRejectionReason().trim().isEmpty()) {
                throw new IllegalArgumentException("Rejection reason is required when rejecting a claim");
            }

            claim.setStatus(WarrantyClaimStatus.REJECTED);
            claim.setRejectionReason(request.getRejectionReason());

            // Gửi notification cho SC_STAFF tạo claim
            if (claim.getCreatedByUserId() != null) {
                notificationService.sendClaimRejectedNotificationToStaff(
                        claim.getId(),
                        claim.getCustomerName(),
                        request.getRejectionReason(),
                        claim.getCreatedByUserId());
            }

        } else {
            throw new IllegalArgumentException("Invalid action. Must be 'APPROVE' or 'REJECT'");
        }

        WarrantyClaim updatedClaim = warrantyClaimRepository.save(claim);
        return warrantyClaimMapper.toResponseWarrantyClaim(updatedClaim);
    }

    @Override
    public WarrantyClaimResponseDTO updateRequiredPart(String claimId, String requiredPart) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found with ID: " + claimId));

        claim.setRequiredParts(requiredPart);
        WarrantyClaim updatedClaim = warrantyClaimRepository.save(claim);

        return warrantyClaimMapper.toResponseWarrantyClaim(updatedClaim);
    }

    @Override
    @Transactional
    public WarrantyClaimResponseDTO assignScTech(String claimId, String scTechId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found with ID: " + claimId));

        SCTechnician scTechnician = scTechnicianRepository.findById(scTechId)
                .orElseThrow(() -> new ResourceNotFoundException("SC Tech not found with ID: " + scTechId));

        claim.setTechnician(scTechnician);
        WarrantyClaim updatedWarrantyClaim = warrantyClaimRepository.save(claim);

        // Send notification to technician
        if (scTechnician.getUserId() != null) {
            notificationService.sendClaimAssignedNotificationToTechnician(
                    claimId,
                    claim.getCustomerName(),
                    claim.getVehicle() != null ? claim.getVehicle().getName() : "N/A",
                    scTechnician.getUserId());
        }

        return warrantyClaimMapper.toResponseWarrantyClaim(claim);
    }

    @Override
    public WarrantyClaimResponseDTO updateReturnDate(String claimId, LocalDate returnDate) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy claim với ID: " + claimId));

        claim.setReturnDate(returnDate);
        WarrantyClaim updatedClaim = warrantyClaimRepository.save(claim);

        return warrantyClaimMapper.toResponseWarrantyClaim(updatedClaim);
    }

//    @Override
//    @Transactional
//    public WarrantyClaimResponseDTO startWork(String claimId, String technicianUsername) {
//        // 1. Tìm claim
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy claim với ID: " + claimId));
//
//        // 2. Validation: Check status phải là APPROVED
//        if (claim.getStatus() != WarrantyClaimStatus.APPROVED) {
//            throw new IllegalStateException(
//                    "Chỉ có thể bắt đầu công việc khi claim đã được duyệt. Trạng thái hiện tại: " + claim.getStatus());
//        }
//
//        // 3. Validation: Check technician đã được assign chưa
//        if (claim.getTechnician() == null) {
//            throw new IllegalStateException("Claim chưa được phân công cho kỹ thuật viên nào");
//        }
//
//        // 4. Validation: Check xem technician có đúng người được assign không
//        // Lấy user từ userId của technician
//        Long technicianUserId = claim.getTechnician().getUserId();
//        if (technicianUserId == null) {
//            throw new IllegalStateException("Technician chưa liên kết với User account");
//        }
//
//        // Tìm user bằng username (email trong hệ thống)
//        User currentUser = userRepository.findByEmail(technicianUsername)
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user: " + technicianUsername));
//
//        // So sánh userId
//        if (!currentUser.getId().equals(technicianUserId)) {
//            throw new IllegalStateException(
//                    "Bạn không được phân công cho claim này. Vui lòng kiểm tra lại.");
//        }
//
//        // 5. Update status và lưu thời gian bắt đầu
//        claim.setStatus(WarrantyClaimStatus.IN_PROGRESS);
//        claim.setWorkStartTime(LocalDateTime.now());
//
//        // 6. Lưu vào database
//        WarrantyClaim updatedClaim = warrantyClaimRepository.save(claim);
//
//        // 7. Gửi notification (optional - comment out nếu method chưa có)
//        // if (claim.getCreatedByUserId() != null) {
//        // notificationService.sendWorkStartedNotification(
//        // claim.getId(),
//        // claim.getCustomerName(),
//        // currentUser.getUsernameDisplay(),
//        // claim.getCreatedByUserId()
//        // );
//        // }
//
//        return warrantyClaimMapper.toResponseWarrantyClaim(updatedClaim);
//    }

//    @Override
//    @Transactional
//    public void deleteClaim(String claimId) {
//        // Kiểm tra claim có tồn tại không
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy claim với ID: " + claimId));
//
//        // Xóa claim
//        warrantyClaimRepository.delete(claim);
//    }

    // thong ke ti le hong theo khu vuc

    @Override
    public List<OfficeBranchFailureStatsDTO> getFailureStatsByOfficeBranch() {
        List<WarrantyClaimStatus> validStatuses = List.of(
                WarrantyClaimStatus.APPROVED,
                WarrantyClaimStatus.REJECTED,
                WarrantyClaimStatus.COMPLETED
        );

        long totalClaims = warrantyClaimRepository.countByStatusIn(validStatuses);
        if (totalClaims == 0) return Collections.emptyList();

        List<Object[]> rows = warrantyClaimRepository.getClaimCountByOfficeBranch(validStatuses);

        return rows.stream().map(row -> {
            OfficeBranch branch = (OfficeBranch) row[0];
            int claimsHandled = ((Long) row[1]).intValue();
            double failureRate = totalClaims == 0 ? 0 :
                    Math.round(((double) claimsHandled / totalClaims) * 100.0) / 100.0;

            OfficeBranchFailureStatsDTO dto = new OfficeBranchFailureStatsDTO();
            dto.setBranchName(branch.getBranchName());
            dto.setAddress(branch.getAddress());
            dto.setTotalClaims((int) totalClaims);
            dto.setClaimsHandled(claimsHandled);
            dto.setFailureRate(failureRate);

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WarrantyClaimResponseDTO startWork(String claimId, String technicianUsername) {
        // 1. Tìm claim
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy claim với ID: " + claimId));

        // 2. Validation: Check status phải là APPROVED
        if (claim.getStatus() != WarrantyClaimStatus.APPROVED) {
            throw new IllegalStateException(
                    "Chỉ có thể bắt đầu công việc khi claim đã được duyệt. Trạng thái hiện tại: " + claim.getStatus());
        }

        // 3. Validation: Check technician đã được assign chưa
        if (claim.getTechnician() == null) {
            throw new IllegalStateException("Claim chưa được phân công cho kỹ thuật viên nào");
        }

        // 4. Validation: Check xem technician có đúng người được assign không
        // Lấy user từ userId của technician
        Long technicianUserId = claim.getTechnician().getUserId();
        if (technicianUserId == null) {
            throw new IllegalStateException("Technician chưa liên kết với User account");
        }

        // Tìm user bằng username (email trong hệ thống)
        User currentUser = userRepository.findByEmail(technicianUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user: " + technicianUsername));

        // So sánh userId
        if (!currentUser.getId().equals(technicianUserId)) {
            throw new IllegalStateException(
                    "Bạn không được phân công cho claim này. Vui lòng kiểm tra lại.");
        }

        // 5. Update status và lưu thời gian bắt đầu
        claim.setStatus(WarrantyClaimStatus.IN_PROGRESS);
        claim.setWorkStartTime(LocalDateTime.now());

        // 6. Lưu vào database
        WarrantyClaim updatedClaim = warrantyClaimRepository.save(claim);

        // 7. Gửi notification (optional - comment out nếu method chưa có)
        // if (claim.getCreatedByUserId() != null) {
        // notificationService.sendWorkStartedNotification(
        // claim.getId(),
        // claim.getCustomerName(),
        // currentUser.getUsernameDisplay(),
        // claim.getCreatedByUserId()
        // );
        // }

        return warrantyClaimMapper.toResponseWarrantyClaim(updatedClaim);
    }

    @Override
    @Transactional
    public void deleteClaim(String claimId) {
        // Kiểm tra claim có tồn tại không
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy claim với ID: " + claimId));

        // Xóa claim
        warrantyClaimRepository.delete(claim);
    }

}
