package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCreateDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyListDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyResponseDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyUpdateDTO;
import com.warrantyclaim.warrantyclaim_api.service.WarrantyPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/warranty-policies")
@CrossOrigin(origins = "*")

public class WarrantyPolicyController {
    private final WarrantyPolicyService service;

    @PostMapping
    public ResponseEntity<WarrantyPolicyResponseDTO> create(@RequestBody WarrantyPolicyCreateDTO createDTO) {
        WarrantyPolicyResponseDTO response = service.createWarrantyPolicy(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<WarrantyPolicyResponseDTO> getById(@PathVariable String id) {
        WarrantyPolicyResponseDTO response = service.getWarrantyPolicyById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarrantyPolicyResponseDTO> update(
            @PathVariable String id,
            @RequestBody WarrantyPolicyUpdateDTO updateDTO) {
        WarrantyPolicyResponseDTO response = service.updateWarrantyPolicy(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteWarrantyPolicy(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<Page<WarrantyPolicyListDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "coverageDurationMonths") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<WarrantyPolicyListDTO> policies = service.getAllWarrantyPolicies(pageable);
        return ResponseEntity.ok(policies);
    }

//-------------------------vehicle-types----------------------------------------------
    @DeleteMapping("/{id}/vehicle-types/{vehicleTypeId}")
    public ResponseEntity<WarrantyPolicyResponseDTO> removeVehicleType(
            @PathVariable String id,
            @PathVariable String vehicleTypeId) {
        WarrantyPolicyResponseDTO response = service.removeVehicleType(id, vehicleTypeId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/vehicle-types")
    public ResponseEntity<WarrantyPolicyResponseDTO> assignVehicleTypes(
            @PathVariable String id,
            @RequestParam  List<String> warrantyPolicyVehicleTypes) {
        WarrantyPolicyResponseDTO response = service.assignVehicleTypes(id, warrantyPolicyVehicleTypes);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/{id}/vehicle-types/{vehicleTypeId}")
    public ResponseEntity<WarrantyPolicyResponseDTO> addVehicleType(
            @PathVariable String id,
            @PathVariable String vehicleTypeId) {
        WarrantyPolicyResponseDTO response = service.addVehicleType(id, vehicleTypeId);
        return ResponseEntity.ok(response);
    }
// ---------------------------------spare-parts-sc-------------------------------------------------------


    @PutMapping("/{id}/spare-parts-sc")
    public ResponseEntity<WarrantyPolicyResponseDTO> assignSparePartsTypeSC(
            @PathVariable String id,
            @RequestParam List<String> warrantyPolicySparePartsTypeSCIds) {
        WarrantyPolicyResponseDTO response = service.assignSparePartsTypeSC(id, warrantyPolicySparePartsTypeSCIds);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/{id}/spare-parts-sc/{sparePartsTypeId}")
    public ResponseEntity<WarrantyPolicyResponseDTO> addSparePartsTypeSC(
            @PathVariable String id,
            @PathVariable String sparePartsTypeId) {
        WarrantyPolicyResponseDTO response = service.addSparePartsTypeSC(id, sparePartsTypeId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/spare-parts-sc/{sparePartsTypeId}")
    public ResponseEntity<WarrantyPolicyResponseDTO> removeSparePartsTypeSC(
            @PathVariable String id,
            @PathVariable String sparePartsTypeId) {
        WarrantyPolicyResponseDTO response = service.removeSparePartsTypeSC(id, sparePartsTypeId);
        return ResponseEntity.ok(response);
    }
// ---------------------------------spare-parts-evm-------------------------------------------------------
    @PutMapping("/{id}/spare-parts-evm")
    public ResponseEntity<WarrantyPolicyResponseDTO> assignSparePartsTypeEVM(
            @PathVariable String id,
            @RequestParam List<String> warrantyPolicySparePartsTypeEVM) {
        WarrantyPolicyResponseDTO response = service.assignSparePartsTypeEVM(id, warrantyPolicySparePartsTypeEVM);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/spare-parts-evm/{sparePartsTypeId}")
    public ResponseEntity<WarrantyPolicyResponseDTO> addSparePartsTypeEVM(
            @PathVariable String id,
            @PathVariable String sparePartsTypeId) {
        WarrantyPolicyResponseDTO response = service.addSparePartsTypeEVM(id, sparePartsTypeId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/spare-parts-evm/{sparePartsTypeId}")
    public ResponseEntity<WarrantyPolicyResponseDTO> removeSparePartsTypeEVM(
            @PathVariable String id,
            @PathVariable String sparePartsTypeId) {
        WarrantyPolicyResponseDTO response = service.removeSparePartsTypeEVM(id, sparePartsTypeId);
        return ResponseEntity.ok(response);
    }

    // ============= Warranty Eligibility Check =============

    /**
     * GET /api/warranty-policies/check?vehicleTypeId=xxx
     * Kiểm tra warranty policy cho một loại xe
     */
    @GetMapping("/check")
    public ResponseEntity<com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO> checkWarrantyEligibility(
            @RequestParam String vehicleTypeId) {
        com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO response = service
                .checkWarrantyByVehicleType(vehicleTypeId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/warranty-policies/check-vin/{vehicleVIN}
     * Kiểm tra warranty policy cho một xe cụ thể bằng VIN
     */
    @GetMapping("/check-vin/{vehicleVIN}")
    public ResponseEntity<com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO> checkWarrantyByVIN(
            @PathVariable String vehicleVIN) {
        try {
            com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO response = service
                    .checkWarrantyEligibility(vehicleVIN);
            return ResponseEntity.ok(response);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO.builder()
                            .isEligible(false)
                            .message("VIN-based checking not yet implemented")
                            .build());
        }
    }


}
