package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface WarrantyClaimService {

    public WarrantyClaimResponseDTO createWarrantyClaim(WarrantyClaimCreateRequestDTO warrantyClaimRequest);

    public Page<WarrantyClaimListResponseDTO> getAllClaims(Pageable pageable);

    public WarrantyClaimResponseDTO updateClaim(String claimId, WarrantyClaimUpdateRequestDTO request);

    public WarrantyClaimDetailResponseDTO getClaimById(String claimId);

    public WarrantyClaimResponseDTO updateClaimStatus(String claimId, WarrantyClaimStatus status);

    public WarrantyClaimResponseDTO approveOrRejectClaim(ApproveRejectClaimRequest request);

    public WarrantyClaimResponseDTO updateRequiredPart(String claimId, String requiredPart);

    public WarrantyClaimResponseDTO assignScTech(String claimId, String scTechId);



    public WarrantyClaimResponseDTO updateReturnDate(String claimId, LocalDate returnDate);



}
