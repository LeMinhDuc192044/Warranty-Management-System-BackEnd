package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.PartsRequestCreateDTO;
import com.warrantyclaim.warrantyclaim_api.dto.PartsRequestResponseDTO;
import com.warrantyclaim.warrantyclaim_api.dto.PartsRequestUpdateDTO;
import com.warrantyclaim.warrantyclaim_api.dto.PartsResponseListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartsRequestService {
    public PartsRequestResponseDTO createPartsRequest(PartsRequestCreateDTO request);

    public PartsRequestResponseDTO getPartsRequestById(String id);

    public Page<PartsResponseListDTO> getAllPartsRequests(Pageable pageable);

    public PartsRequestResponseDTO updatePartsRequest(String id, PartsRequestUpdateDTO request);

    public void deletePartsRequest(String id);

    public PartsRequestResponseDTO approvePartsRequest(String id);

    public PartsRequestResponseDTO rejectPartsRequest(String id, String reason);
}
