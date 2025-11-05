package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicle;
import com.warrantyclaim.warrantyclaim_api.entity.SCTechnician;
import com.warrantyclaim.warrantyclaim_api.entity.WarrantyClaim;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.mapper.WarrantyClaimMapper;
import com.warrantyclaim.warrantyclaim_api.repository.ElectricVehicleRepository;
import com.warrantyclaim.warrantyclaim_api.repository.SCTechnicianRepository;
import com.warrantyclaim.warrantyclaim_api.repository.WarrantyClaimRepository;
import com.warrantyclaim.warrantyclaim_api.service.NotificationService;
import com.warrantyclaim.warrantyclaim_api.service.WarrantyClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarrantyClaimServiceImp implements WarrantyClaimService {

    private final WarrantyClaimRepository warrantyClaimRepository;
    private final ElectricVehicleRepository electricVehicleRepository;
    private final SCTechnicianRepository scTechnicianRepository;
    private final WarrantyClaimMapper warrantyClaimMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public WarrantyClaimResponseDTO createWarrantyClaim(WarrantyClaimCreateRequestDTO warrantyClaimRequest) {

        ElectricVehicle electricVehicle = electricVehicleRepository.findById(warrantyClaimRequest.getVehicleId()) // for
                                                                                                                  // testing
                                                                                                                  // exception
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
        Page<WarrantyClaim> claims = warrantyClaimRepository.findAll(pageable);
        return claims.map(warrantyClaimMapper::toListResponse);
    }

    @Override
    public WarrantyClaimResponseDTO updateClaim(String claimId, WarrantyClaimUpdateRequestDTO request) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found with ID: " + claimId));

        if (request.getElectricVehicleId() != null) {
            ElectricVehicle electricVehicle = electricVehicleRepository.findById(request.getElectricVehicleId())
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

        //  Thêm đoạn kiểm tra ngay sau khi lấy claim
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

            // Gửi notification cho SC_STAFF tạo claim
            if (claim.getCreatedByUserId() != null) {
                notificationService.sendClaimApprovedNotificationToStaff(
                        claim.getId(),
                        claim.getCustomerName(),
                        claim.getCreatedByUserId());
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
    public WarrantyClaimResponseDTO assignScTech(String claimId, String scTechId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found with ID: " + claimId));

        SCTechnician scTechnician = scTechnicianRepository.findById(scTechId)
                .orElseThrow(() -> new ResourceNotFoundException("SC Tech not found with ID: " + scTechId));

        claim.setTechnician(scTechnician);
        WarrantyClaim updatedWarrantyClaim = warrantyClaimRepository.save(claim);
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


}
