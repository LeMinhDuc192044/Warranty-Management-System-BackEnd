package com.warrantyclaim.warrantyclaim_api.enums;

public enum RecallVehicleStatus {
    PENDING("Pending"),
    NOTIFIED("Notified"),
    SCHEDULED("Scheduled"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    RecallVehicleStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
