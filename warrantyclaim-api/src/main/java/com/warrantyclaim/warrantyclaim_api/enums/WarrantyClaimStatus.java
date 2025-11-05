package com.warrantyclaim.warrantyclaim_api.enums;

public enum WarrantyClaimStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    APPROVED("Approved"),
    ASSIGNED_TO_TECHNICIAN("Assigned to Technician"),
    INSPECTION_COMPLETED("Inspection Completed"),
    PENDING_PARTS("Pending Parts"),
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
