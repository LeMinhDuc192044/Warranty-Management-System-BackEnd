package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeBranchFailureStatsDTO {
    private String branchName;
    private String address;
    private int totalClaims;     // Tổng toàn hệ thống
    private int claimsHandled;   // Số lượng do chi nhánh này xử lý
    private double failureRate;  // Tỉ lệ hỏng hóc tại chi nhánh

    public OfficeBranchFailureStatsDTO(OfficeBranch branch, int totalClaims, int claimsHandled) {
        this.branchName = branch.getBranchName();
        this.address = branch.getAddress();
        this.totalClaims = totalClaims;
        this.claimsHandled = claimsHandled;
        this.failureRate = totalClaims == 0 ? 0 : (double) claimsHandled / totalClaims;
    }
}

