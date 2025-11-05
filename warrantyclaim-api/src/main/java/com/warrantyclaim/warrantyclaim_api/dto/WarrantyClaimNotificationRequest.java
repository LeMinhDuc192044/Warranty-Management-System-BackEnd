package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyClaimNotificationRequest {

    private String claimId;
    private String customerName;
    private String branchOffice;
    private String createdBy;
    private String priority; // "normal", "high", "urgent"
}
