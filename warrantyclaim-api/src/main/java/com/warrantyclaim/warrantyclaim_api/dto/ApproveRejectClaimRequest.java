package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveRejectClaimRequest {
    private String claimId;
    private String action; // "APPROVE" or "REJECT"
    private String rejectionReason; // Bắt buộc nếu action = "REJECT"
    private Long approvedByUserId; // ID của SC_ADMIN đang duyệt
}
