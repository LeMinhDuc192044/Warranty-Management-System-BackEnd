package com.warrantyclaim.warrantyclaim_api.enums;

public enum UsageType {
    PERSONAL("Personal use"),
    COMMERCIAL("Commercial use");

    private final String displayName;

    UsageType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
