package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartsRequestService {
    public PartsRequestResponseDTO createPartsRequest(PartsRequestCreateDTO request);

    public PartsRequestResponseDTO getPartsRequestById(String id);

    public Page<PartsResponseListDTO> getAllPartsRequests(Pageable pageable);

    public PartsRequestResponseDTO updatePartsRequest(String id, PartsRequestUpdateDTO request);

    public void deletePartsRequest(String id);

    /**
     * @deprecated Use evmStaffApproveRequest() instead for proper tracking workflow
     */
    @Deprecated
    public PartsRequestResponseDTO approvePartsRequest(String id);

    /**
     * @deprecated Use evmStaffRejectRequest() instead for proper tracking workflow
     */
    @Deprecated
    public PartsRequestResponseDTO rejectPartsRequest(String id, String reason);

    /**
     * EVM_STAFF approve parts request with inventory tracking
     * - Validates stock availability
     * - Does NOT deduct EVM inventory yet (wait for SC_ADMIN confirmation)
     * - Updates request status to APPROVED/SHIPPING
     * - Sends notification to SC_ADMIN
     */
    public PartsRequestResponseDTO evmStaffApproveRequest(PartsRequestApprovalDTO approvalDTO);

    /**
     * EVM_STAFF reject parts request with reason
     * - Updates request status to REJECTED
     * - Saves rejection reason
     * - Sends notification to SC_ADMIN with reason
     */
    public PartsRequestResponseDTO evmStaffRejectRequest(PartsRequestApprovalDTO approvalDTO);

    /**
     * SC_ADMIN confirms receiving parts
     * - Deducts EVM inventory
     * - Adds to SC inventory
     * - Updates status to DELIVERED
     * - Logs stock before/after for both warehouses
     * - Sends confirmation notification
     */
    public PartsRequestResponseDTO scAdminConfirmReceive(PartsRequestReceiveDTO receiveDTO);
}
