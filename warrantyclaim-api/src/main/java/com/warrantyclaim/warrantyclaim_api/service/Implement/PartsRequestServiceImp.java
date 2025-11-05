package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.*;
import com.warrantyclaim.warrantyclaim_api.enums.RecallStatus;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.mapper.PartsRequestMapper;
import com.warrantyclaim.warrantyclaim_api.repository.ElectricVehicleRepository;
import com.warrantyclaim.warrantyclaim_api.repository.PartsRequestRepository;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsTypeEVMRepository;
import com.warrantyclaim.warrantyclaim_api.repository.UserRepository;
import com.warrantyclaim.warrantyclaim_api.service.NotificationService;
import com.warrantyclaim.warrantyclaim_api.service.PartsRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class PartsRequestServiceImp implements PartsRequestService {

    private final PartsRequestRepository partsRequestRepository;
    private final ProductsSparePartsTypeEVMRepository partTypeRepository;
    private final PartsRequestMapper partsRequestMapper;
    private final ElectricVehicleRepository electricVehicleRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Transactional
    public PartsRequestResponseDTO createPartsRequest(PartsRequestCreateDTO request) {
        // 1. Validate EVM part type exists
        ProductsSparePartsTypeEVM partType = partTypeRepository.findById(request.getPartTypeId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Part type not found with ID: " + request.getPartTypeId()));

        // Create entity
        PartsRequest partsRequest = partsRequestMapper.toEntity(request);

        // 2. Validate and set vehicle using VIN
        if (request.getVin() != null) {
            ElectricVehicle electricVehicle = electricVehicleRepository.findById(request.getVin())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Electric vehicle not found with VIN: " + request.getVin()));
            partsRequest.setElectricVehicle(electricVehicle);
        }

        // 3. Update entity
        partsRequest.setId(generateRequestId());
        partsRequest.setPartType(partType);

        // 4. Save
        PartsRequest savedRequest = partsRequestRepository.save(partsRequest);

        // 5. Send notification to EVM_STAFF
        try {
            // Get SC_ADMIN user name from requestedByStaffId
            String scAdminName = "SC_ADMIN";
            if (request.getRequestedByStaffId() != null) {
                userRepository.findById(Long.parseLong(request.getRequestedByStaffId()))
                        .ifPresent(user -> {
                            // Use username or email as SC_ADMIN name
                        });
            }

            notificationService.sendPartsRequestCreatedNotificationToEVMStaff(
                    savedRequest.getId(),
                    scAdminName,
                    request.getBranchOffice(),
                    request.getPartName(),
                    request.getQuantity());
        } catch (Exception e) {
            // Log error but don't fail the request creation
            System.err.println("Failed to send notification: " + e.getMessage());
        }

        return partsRequestMapper.toResponseDTO(savedRequest);
    }

    /**
     * Get parts request by ID
     */
    @Transactional(readOnly = true)
    public PartsRequestResponseDTO getPartsRequestById(String id) {
        PartsRequest partsRequest = partsRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parts request not found with ID: " + id));

        return partsRequestMapper.toResponseDTO(partsRequest);
    }

    /**
     * Get all parts requests (with pagination)
     */
    @Transactional(readOnly = true)
    public Page<PartsResponseListDTO> getAllPartsRequests(Pageable pageable) {
        Page<PartsRequest> requests = partsRequestRepository.findAll(pageable);
        return requests.map(partsRequestMapper::toListDTO);
    }

    /**
     * Get all parts requests (without pagination)
     */
    @Transactional(readOnly = true)
    public List<PartsResponseListDTO> getAllPartsRequests() {
        List<PartsRequest> requests = partsRequestRepository.findAll();
        return requests.stream()
                .map(partsRequestMapper::toListDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update parts request
     */
    @Transactional
    public PartsRequestResponseDTO updatePartsRequest(String id, PartsRequestUpdateDTO request) {
        // 1. Find parts request
        PartsRequest partsRequest = partsRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parts request not found with ID: " + id));

        // 2. Update part type if changed
        if (request.getPartTypeId() != null) {
            ProductsSparePartsTypeEVM partType = partTypeRepository.findById(request.getPartTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Part type not found with ID: " + request.getPartTypeId()));
            partsRequest.setPartType(partType);
        }

        // Update vehicle if VIN changed
        if (request.getVin() != null) {
            ElectricVehicle electricVehicle = electricVehicleRepository.findById(request.getVin())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Electric vehicle not found with VIN: " + request.getVin()));
            partsRequest.setElectricVehicle(electricVehicle);
        }

        partsRequestMapper.updateEntity(partsRequest, request);
        PartsRequest updatedRequest = partsRequestRepository.save(partsRequest);

        return partsRequestMapper.toResponseDTO(updatedRequest);
    }

    /**
     * Delete parts request
     */
    @Transactional
    public void deletePartsRequest(String id) {
        if (!partsRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Parts request not found with ID: " + id);
        }

        partsRequestRepository.deleteById(id);
    }

    /**
     * Approve parts request - automatically decrease stock
     */
    @Transactional
    public PartsRequestResponseDTO approvePartsRequest(String id) {
        // 1. Find parts request
        PartsRequest request = partsRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parts request not found with ID: " + id));

        // 2. Get the part type
        ProductsSparePartsTypeEVM partType = request.getPartType();
        if (partType == null) {
            throw new IllegalStateException("Part type not associated with this request");
        }

        // 3. Check if enough stock
        int currentStock = partType.getTotalAmountOfProduct() != null ? partType.getTotalAmountOfProduct() : 0;
        int requestedQuantity = request.getQuantity() != null ? request.getQuantity() : 0;

        if (currentStock < requestedQuantity) {
            throw new IllegalStateException(
                    String.format("Insufficient stock. Available: %d, Requested: %d", currentStock, requestedQuantity));
        }

        // 4. Decrease stock
        int newStock = currentStock - requestedQuantity;
        partType.setTotalAmountOfProduct(newStock);

        // 5. Auto-update stock status based on quantity
        String newStatus;
        if (newStock == 0) {
            newStatus = "OUT_OF_STOCK";
        } else if (newStock <= 10) {
            newStatus = "LOW_STOCK";
        } else {
            newStatus = "IN_STOCK";
        }
        partType.setStockStatus(newStatus);

        // 6. Save part type with updated stock
        partTypeRepository.save(partType);

        // 7. Update request status to APPROVED
        request.setStatus(com.warrantyclaim.warrantyclaim_api.enums.PartsRequestStatus.APPROVED);
        PartsRequest approvedRequest = partsRequestRepository.save(request);

        // 8. Send notification to SC_ADMIN
        try {
            if (approvedRequest.getRequestedByStaffId() != null) {
                notificationService.sendPartsRequestApprovedNotificationToSCAdmin(
                        approvedRequest.getId(),
                        approvedRequest.getPartName(),
                        approvedRequest.getRequestedByStaffId());
            }
        } catch (Exception e) {
            // Log error but don't fail the approval
            System.err.println("Failed to send approval notification: " + e.getMessage());
        }

        return partsRequestMapper.toResponseDTO(approvedRequest);
    }

    /**
     * Reject parts request
     */
    @Transactional
    public PartsRequestResponseDTO rejectPartsRequest(String id, String reason) {
        PartsRequest request = partsRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parts request not found with ID: " + id));

        request.setStatus(com.warrantyclaim.warrantyclaim_api.enums.PartsRequestStatus.REJECTED);
        // TODO: Add rejection reason field if needed

        PartsRequest rejectedRequest = partsRequestRepository.save(request);

        // Send notification to SC_ADMIN
        try {
            if (rejectedRequest.getRequestedByStaffId() != null) {
                notificationService.sendPartsRequestRejectedNotificationToSCAdmin(
                        rejectedRequest.getId(),
                        rejectedRequest.getPartName(),
                        reason,
                        rejectedRequest.getRequestedByStaffId());
            }
        } catch (Exception e) {
            // Log error but don't fail the rejection
            System.err.println("Failed to send rejection notification: " + e.getMessage());
        }

        return partsRequestMapper.toResponseDTO(rejectedRequest);
    }

    /**
     * Generate unique request ID
     */
    private String generateRequestId() {
        return "PR-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
