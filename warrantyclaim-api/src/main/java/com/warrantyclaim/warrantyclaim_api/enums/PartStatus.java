package com.warrantyclaim.warrantyclaim_api.enums;


public enum PartStatus {
    ACTIVE("Active", "Part is currently in production and available."),
    IN_PRODUCTION("In Production", "Part is actively being manufactured."),
    OBSOLETE("Obsolete", "Part is no longer in production, replaced by newer version."),
    SUSPENDED("Suspended", "Part temporarily on hold due to quality or supply issues."),
    DELETED("Deleted", "Part has been removed from the system."),
    BROKEN("Broken", "Part is damaged and requires replacement or repair."),
    REPLACED("Replaced", "Part is replaced"),
    TRANSFERRED("Transferred", "Part is transferred to EVM");


    private final String displayName;
    private final String description;

    PartStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
