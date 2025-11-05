package com.warrantyclaim.warrantyclaim_api.enums;

public enum WarrantyClaimStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    WarrantyClaimStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
