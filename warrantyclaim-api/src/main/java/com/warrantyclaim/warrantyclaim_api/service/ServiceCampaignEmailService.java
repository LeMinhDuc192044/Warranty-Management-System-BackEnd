package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.CampaignEmailRequest;
import com.warrantyclaim.warrantyclaim_api.dto.CampaignEmailResponse;

public interface ServiceCampaignEmailService {
    public CampaignEmailResponse sendCampaignEmail(CampaignEmailRequest request);
    public CampaignEmailResponse sendHtmlCampaignEmail(CampaignEmailRequest request);
    public CampaignEmailResponse sendCampaignEmailBulk(CampaignEmailRequest request);

}
