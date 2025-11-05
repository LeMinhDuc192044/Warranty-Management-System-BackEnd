package com.warrantyclaim.warrantyclaim_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailNotificationRequestDTO {
    @NotBlank
    private String claimId;
}
