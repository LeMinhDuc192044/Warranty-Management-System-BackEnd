package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateUserStatusRequest {
    @NotNull(message = "Status không được để trống")
    private AccountStatus status;

    private String reason;

    public UpdateUserStatusRequest() {
    }

    public UpdateUserStatusRequest(AccountStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
