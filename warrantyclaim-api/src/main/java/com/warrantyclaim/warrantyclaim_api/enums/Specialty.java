package com.warrantyclaim.warrantyclaim_api.enums;

public enum Specialty {


    ELECTRIC_MOTOR_SYSTEMS("Electric motor drive & inverter systems specialist"),
    BATTERY_SYSTEMS("High-voltage battery & BMS diagnostics specialist"),
    ELECTRONICS_AND_ECU("Vehicle ECU & electrical system troubleshooting specialist"),
    BRAKE_SYSTEMS("Brake & regenerative braking service specialist"),
    SUSPENSION_AND_STEERING("Suspension & steering mechanical specialist"),
    BODY_AND_FRAME("Body repair & structural adjustment specialist"),
    INTERIOR_AND_SAFETY("Interior components & vehicle safety systems specialist");

    private final String description;

    Specialty(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
