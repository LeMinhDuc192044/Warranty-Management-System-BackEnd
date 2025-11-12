package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;

public record PartTypeAndPartStatusCountEVMResponse(
        String partTypeId,
        Long count,
        PartStatus condition
) {
}
