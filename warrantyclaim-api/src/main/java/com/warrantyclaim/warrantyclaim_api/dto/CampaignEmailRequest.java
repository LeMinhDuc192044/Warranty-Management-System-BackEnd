package com.warrantyclaim.warrantyclaim_api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to send service campaign email")
public class CampaignEmailRequest {

    @NotBlank(message = "Campaign name is required")
    @Schema(description = "Campaign name", example = "Summer Service Campaign 2025")
    private String campaignName;

    @NotEmpty(message = "Recipients list cannot be empty")
    @Size(min = 1, max = 100, message = "Recipients must be between 1 and 100")
    @Schema(description = "List of recipient email addresses")
    private List<String> recipients;

    @NotBlank(message = "Subject is required")
    @Schema(description = "Email subject", example = "VinFast Special Service Offer")
    private String subject;

    @NotBlank(message = "Content is required")
    @Schema(description = "Email content (text or HTML)")
    private String content;

    @Schema(description = "Is HTML content?", example = "false")
    private boolean isHtml = false;
}
