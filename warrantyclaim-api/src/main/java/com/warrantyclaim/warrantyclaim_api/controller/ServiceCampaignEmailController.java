package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.CampaignEmailRequest;
import com.warrantyclaim.warrantyclaim_api.dto.CampaignEmailResponse;
import com.warrantyclaim.warrantyclaim_api.service.ServiceCampaignEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campaigns/email")
@RequiredArgsConstructor
@Tag(name = "Service Campaign Email", description = "Send campaign emails to multiple customers")
public class ServiceCampaignEmailController {
    private final ServiceCampaignEmailService service;

    @PostMapping("/send")
    @Operation(summary = "Send service campaign email to multiple recipients")
    public ResponseEntity<CampaignEmailResponse> sendCampaignEmail(
            @RequestBody @Valid CampaignEmailRequest request) {

        CampaignEmailResponse response;

        if (request.isHtml()) {
            response = service.sendHtmlCampaignEmail(request);
        } else {
            response = service.sendCampaignEmail(request);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-bulk")
    @Operation(summary = "Send campaign email in bulk (all at once)")
    public ResponseEntity<CampaignEmailResponse> sendCampaignEmailBulk(
            @RequestBody @Valid CampaignEmailRequest request) {

        CampaignEmailResponse response = service.sendCampaignEmailBulk(request);
        return ResponseEntity.ok(response);
    }
}
