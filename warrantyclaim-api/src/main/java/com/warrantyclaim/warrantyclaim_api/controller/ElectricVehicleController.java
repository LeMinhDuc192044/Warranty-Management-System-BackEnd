package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.service.ElectricVehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Configure as needed
@RequestMapping("/api/ElectricVehicle")
// @SecurityRequirement(name = "bearerAuth")
public class ElectricVehicleController {
    private final ElectricVehicleService electricVehicleService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add new electric vehicle with picture")
    public ResponseEntity<VehicleDetailInfo> addElectricVehicle(
            @ModelAttribute @Valid VehicleCreateDTO vehicleCreateDTO) throws IOException {

        VehicleDetailInfo result = electricVehicleService
                .addElectricVehicle(vehicleCreateDTO, vehicleCreateDTO.getUrlPicture());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}/return-date")
    public ResponseEntity<ElectricVehicleResponseDTO> updateReturnDate(
            @PathVariable("id") String id,
            @RequestParam @Valid LocalDate returnDate) {

        ElectricVehicleResponseDTO response = electricVehicleService
                .updateReturnDate(id, returnDate);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ElectricVehicleListResponseDTO>> getAllVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ElectricVehicleListResponseDTO> response = electricVehicleService.getAllVehicles(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectricVehicleResponseDTO> getVehicleById(@PathVariable String id) {
        ElectricVehicleResponseDTO response = electricVehicleService.getVehicleById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ElectricVehicleResponseDTO> updateVehicle(
            @PathVariable String id,
            @RequestParam ElectricVehicleUpdateRequestDTO request,
            @ModelAttribute MultipartFile urlPicture) {
        System.out.println("✅ Update Controller reached for vehicle: " + id);
        System.out.println("📦 Update request data: " + request);
        System.out.println("📅 ProductionDate in request: " + request.getProductionDate());
        ElectricVehicleResponseDTO response = electricVehicleService.updateVehicle(id, request, urlPicture);
        System.out.println("📤 Update response purchaseDate: " + response.getPurchaseDate());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String id) {
        electricVehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
