package com.warrantyclaim.warrantyclaim_api.enums;

public enum CoverageTypeWarrantyPolicy {
    FULL("Full vehicle coverage"),
    LIMITED("Limited coverage"),
    BATTERY("Battery only"),
    PARTS_ONLY("Parts only"),
    BODY("Body corrosion"),
    PAINT("Exterior paint"),
    SUSPENSION("Suspension system"),
    ACCESSORY("Genuine accessories"),
    REGULATION("Warranty conditions"),
    EXCLUSION("Warranty exclusions"),
    NONE("No coverage");

    private final String displayName;

    CoverageTypeWarrantyPolicy(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
