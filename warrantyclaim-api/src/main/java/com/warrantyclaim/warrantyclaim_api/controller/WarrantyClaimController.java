package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import com.warrantyclaim.warrantyclaim_api.service.WarrantyClaimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Configure as needed
@RequestMapping("/api/WarrantyClaim")
public class WarrantyClaimController {

    private final WarrantyClaimService warrantyClaimService;

    @PostMapping
    public ResponseEntity<WarrantyClaimResponseDTO> createWarrantyClaim(
            @RequestBody WarrantyClaimCreateRequestDTO warrantyClaimCreateRequestDTO) {
        System.out.println("✅ Controller reached: ");
        WarrantyClaimResponseDTO saveWarrantyClaim = warrantyClaimService
                .createWarrantyClaim(warrantyClaimCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveWarrantyClaim);
    }

    @GetMapping
    public ResponseEntity<Page<WarrantyClaimListResponseDTO>> getAllClaims(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "claimDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<WarrantyClaimListResponseDTO> response = warrantyClaimService.getAllClaims(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<WarrantyClaimDetailResponseDTO> getClaimById(@PathVariable String claimId) {
        WarrantyClaimDetailResponseDTO response = warrantyClaimService.getClaimById(claimId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{claimId}")
    public ResponseEntity<WarrantyClaimResponseDTO> updateClaim(
            @PathVariable String claimId,
            @RequestBody WarrantyClaimUpdateRequestDTO request) {
        WarrantyClaimResponseDTO response = warrantyClaimService.updateClaim(claimId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{claimId}/status")
    public ResponseEntity<WarrantyClaimResponseDTO> updateClaimStatus(
            @PathVariable String claimId,
            @RequestParam WarrantyClaimStatus status) {
        WarrantyClaimResponseDTO response = warrantyClaimService.updateClaimStatus(claimId, status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/approve-reject")
    public ResponseEntity<WarrantyClaimResponseDTO> approveOrRejectClaim(
            @RequestBody ApproveRejectClaimRequest request) {
        WarrantyClaimResponseDTO response = warrantyClaimService.approveOrRejectClaim(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{claimId}/required_part")
    public ResponseEntity<WarrantyClaimResponseDTO> updateClaimRequiredPart(
            @PathVariable String claimId,
            @RequestParam String requiredPart) {
        WarrantyClaimResponseDTO response = warrantyClaimService.updateRequiredPart(claimId, requiredPart);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{claimId}/assign-tech")
    public ResponseEntity<WarrantyClaimResponseDTO> assignStaffToClaim(
            @PathVariable String claimId,
            @RequestParam String scTechId) {
        WarrantyClaimResponseDTO response = warrantyClaimService.assignScTech(claimId, scTechId);
        return ResponseEntity.ok(response);
    }

    // techni điền ngày nhận xe cho customer
    @PatchMapping("/{claimId}/return-date")
    public ResponseEntity<WarrantyClaimResponseDTO> updateReturnDate(
            @PathVariable String claimId,
            @RequestParam LocalDate returnDate) {
        WarrantyClaimResponseDTO response = warrantyClaimService.updateReturnDate(claimId, returnDate);
        return ResponseEntity.ok(response);
    }

    // SC Technical bắt đầu công việc: APPROVED → IN_PROGRESS
    @PostMapping("/{claimId}/start-work")
    public ResponseEntity<?> startWork(
            @PathVariable String claimId,
            @RequestParam String technicianUsername) {
        try {
            WarrantyClaimResponseDTO response = warrantyClaimService.startWork(claimId, technicianUsername);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Có lỗi xảy ra khi bắt đầu công việc"));
        }
    }

    // SC_ADMIN xóa yêu cầu bảo hành
    @DeleteMapping("/{claimId}")
    public ResponseEntity<?> deleteClaim(@PathVariable String claimId) {
        try {
            warrantyClaimService.deleteClaim(claimId);
            return ResponseEntity.ok().body(new SuccessResponse("Xóa yêu cầu bảo hành thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Không thể xóa yêu cầu bảo hành: " + e.getMessage()));
        }
    }

    // thống kê tỉ hệ hỏng theo khu vực
    @GetMapping("/statistics/failure-rate")
    public ResponseEntity<List<OfficeBranchFailureStatsDTO>> getFailureRateByOfficeBranch() {
        List<OfficeBranchFailureStatsDTO> stats = warrantyClaimService.getFailureStatsByOfficeBranch();
        return ResponseEntity.ok(stats);
    }

    // Helper class for error responses
    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // Helper class for success responses
    private static class SuccessResponse {
        private final String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
