package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.SerialNumberMappingRequestDTO;
import com.warrantyclaim.warrantyclaim_api.dto.SerialNumberMappingResponseDTO;
import com.warrantyclaim.warrantyclaim_api.service.SerialNumberMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/serial-numbers")
public class SerialNumberMappingController {

    private final SerialNumberMappingService serialNumberMappingService;

    /**
     * POST /api/serial-numbers/map
     * Map serial number vào vehicle
     */
    @PostMapping("/map")
    public ResponseEntity<SerialNumberMappingResponseDTO> mapSerialNumber(
            @RequestBody SerialNumberMappingRequestDTO requestDTO) {
        SerialNumberMappingResponseDTO response = serialNumberMappingService.mapSerialToVehicle(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/serial-numbers/vehicle/{vin}
     * Lấy tất cả serial numbers của một VIN
     */
    @GetMapping("/vehicle/{vin}")
    public ResponseEntity<List<SerialNumberMappingResponseDTO>> getSerialNumbersByVIN(
            @PathVariable String vin) {
        List<SerialNumberMappingResponseDTO> response = serialNumberMappingService.getSerialNumbersByVIN(vin);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/serial-numbers/claim/{claimId}
     * Lấy tất cả serial numbers của một claim
     */
    @GetMapping("/claim/{claimId}")
    public ResponseEntity<List<SerialNumberMappingResponseDTO>> getSerialNumbersByClaim(
            @PathVariable String claimId) {
        List<SerialNumberMappingResponseDTO> response = serialNumberMappingService.getSerialNumbersByClaim(claimId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/serial-numbers/{serialNumber}
     * Lấy chi tiết một serial number
     */
    @GetMapping("/{serialNumber}")
    public ResponseEntity<SerialNumberMappingResponseDTO> getSerialNumberDetails(
            @PathVariable String serialNumber) {
        SerialNumberMappingResponseDTO response = serialNumberMappingService.getSerialNumberDetails(serialNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/serial-numbers/check/{serialNumber}
     * Kiểm tra serial number đã được sử dụng chưa
     */
    @GetMapping("/check/{serialNumber}")
    public ResponseEntity<Boolean> checkSerialNumberUsed(
            @PathVariable String serialNumber) {
        boolean isUsed = serialNumberMappingService.isSerialNumberUsed(serialNumber);
        return ResponseEntity.ok(isUsed);
    }

    /**
     * PATCH /api/serial-numbers/{serialNumber}/durability
     * Cập nhật durability percentage
     */
    @PatchMapping("/{serialNumber}/durability")
    public ResponseEntity<SerialNumberMappingResponseDTO> updateDurability(
            @PathVariable String serialNumber,
            @RequestParam Integer durabilityPercentage) {
        SerialNumberMappingResponseDTO response = serialNumberMappingService.updateDurability(
                serialNumber, durabilityPercentage);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/serial-numbers/{serialNumber}
     * Xóa mapping
     */
    @DeleteMapping("/{serialNumber}")
    public ResponseEntity<Void> deleteMapping(@PathVariable String serialNumber) {
        serialNumberMappingService.deleteMapping(serialNumber);
        return ResponseEntity.noContent().build();
    }
}
