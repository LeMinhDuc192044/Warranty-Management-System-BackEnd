package com.warrantyclaim.warrantyclaim_api.enums;

import lombok.Getter;

@Getter
public enum VehicleType {

    VF3("VF 3 - Mini Electric SUV"),
    VF5("VF 5 - Compact Electric CUV"),
    VF6("VF 6 - Compact Electric SUV"),
    VF7("VF 7 - C-Segment Electric SUV"),
    VF8("VF 8 - D-Segment Electric SUV"),
    VF9("VF 9 - E-Segment Electric SUV");

    private final String description;

    VehicleType(String description) {
        this.description = description;
    }
}
