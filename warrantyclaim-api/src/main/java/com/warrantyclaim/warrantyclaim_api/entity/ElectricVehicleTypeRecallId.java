package com.warrantyclaim.warrantyclaim_api.entity;

import java.io.Serializable;
import java.util.Objects;

public class ElectricVehicleTypeRecallId implements Serializable {

    private String vehicleTypeId;
    private String recallId;

    public ElectricVehicleTypeRecallId() {}

    public ElectricVehicleTypeRecallId(String vehicleTypeId, String recallId) {
        this.vehicleTypeId = vehicleTypeId;
        this.recallId = recallId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElectricVehicleTypeRecallId)) return false;
        ElectricVehicleTypeRecallId that = (ElectricVehicleTypeRecallId) o;
        return Objects.equals(vehicleTypeId, that.vehicleTypeId) &&
                Objects.equals(recallId, that.recallId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleTypeId, recallId);
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getRecallId() {
        return recallId;
    }

    public void setRecallId(String recallId) {
        this.recallId = recallId;
    }
// Optional: getters & setters nếu bạn không dùng Lombok cho class này
}
