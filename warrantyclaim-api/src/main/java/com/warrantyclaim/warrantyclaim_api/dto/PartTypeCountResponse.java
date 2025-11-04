package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;

public record PartTypeCountResponse(
        OfficeBranch branch,
        String partTypeId,
        Long count
) {}
