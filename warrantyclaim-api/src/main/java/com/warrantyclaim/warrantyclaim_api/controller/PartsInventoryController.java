package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.PartAvailabilityItemDTO;
import com.warrantyclaim.warrantyclaim_api.dto.PartsAvailabilityResponseDTO;
import com.warrantyclaim.warrantyclaim_api.service.PartsInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/parts")
public class PartsInventoryController {

    private final PartsInventoryService partsInventoryService;

    /**
     * GET /api/parts/check?claimId=xxx
     * Kiểm tra tình trạng phụ tùng cho một warranty claim
     */
    @GetMapping("/check")
    public ResponseEntity<PartsAvailabilityResponseDTO> checkPartsForClaim(
            @RequestParam String claimId) {
        PartsAvailabilityResponseDTO response = partsInventoryService.checkPartsAvailability(claimId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/parts/check-types?partTypeIds=PART-001,PART-002&branch=BRANCH_HN
     * Kiểm tra tình trạng danh sách part types tại một chi nhánh
     */
    @GetMapping("/check-types")
    public ResponseEntity<List<PartAvailabilityItemDTO>> checkPartTypes(
            @RequestParam List<String> partTypeIds,
            @RequestParam String branch) {
        List<PartAvailabilityItemDTO> response = partsInventoryService.checkPartTypeAvailability(partTypeIds, branch);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/parts/quantity?partTypeId=PART-001&branch=BRANCH_HN
     * Lấy số lượng available của một part type tại chi nhánh
     */
    @GetMapping("/quantity")
    public ResponseEntity<Integer> getAvailableQuantity(
            @RequestParam String partTypeId,
            @RequestParam String branch) {
        Integer quantity = partsInventoryService.getAvailableQuantity(partTypeId, branch);
        return ResponseEntity.ok(quantity);
    }
}
