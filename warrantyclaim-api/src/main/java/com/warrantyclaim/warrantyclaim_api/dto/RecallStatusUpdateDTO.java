package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
;



public class RecallStatusUpdateDTO {
    private String recallId;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecallId() {
        return recallId;
    }

    public void setRecallId(String recallId) {
        this.recallId = recallId;
    }

    public RecallStatusUpdateDTO(String recallId, String status) {
        this.recallId = recallId;
        this.status = status;
    }
}

