package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.Recall;
import com.warrantyclaim.warrantyclaim_api.enums.RecallStatus;
import com.warrantyclaim.warrantyclaim_api.service.RecallService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/recalls")
public class RecallController {

    @Autowired
    private RecallService recallService;

    @PostMapping
    public ResponseEntity<RecallResponseDTO> createRecall(@RequestBody RecallCreateDTO createDTO) {
        RecallResponseDTO response = recallService.createRecall(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // Thêm vào RecallController

    @PatchMapping("/{recallId}/vehicles/{vehicleId}/status")
    public ResponseEntity<RecallVehicleStatusResponseDTO> updateRecallVehicleStatus(
            @PathVariable String recallId,
            @PathVariable String vehicleId,
            @RequestBody UpdateRecallVehicleStatusDTO statusDTO) {

        RecallVehicleStatusResponseDTO response = recallService.updateRecallVehicleStatus(
                recallId, vehicleId, statusDTO);

        return ResponseEntity.ok(response);
    }
// Thêm vào RecallController

    @GetMapping("/{recallId}/vehicles/{vehicleId}")
    public ResponseEntity<RecallVehicleDetailDTO> getRecallVehicleDetail(
            @PathVariable String recallId,
            @PathVariable String vehicleId) {

        RecallVehicleDetailDTO response = recallService.getRecallVehicleDetail(recallId, vehicleId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<RecallResponseDTO> getRecallById(@PathVariable String id) {
        RecallResponseDTO response = recallService.getRecallById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecallResponseDTO> updateRecall(
            @PathVariable String id,
            @RequestBody UpdateRecallRequest updateDTO) {
        RecallResponseDTO response = recallService.updateRecall(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecall(@PathVariable String id) {
        recallService.deleteRecall(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<RecallsListDTO>> getAllRecalls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RecallsListDTO> recalls = recallService.getAllRecalls(pageable);
        return ResponseEntity.ok(recalls);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RecallResponseDTO> updateRecallStatus(
            @PathVariable String id,
            @RequestParam RecallStatus statusDTO) {
        RecallResponseDTO response = recallService.updateRecallStatus(id, statusDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/notification")
    public ResponseEntity<RecallResponseDTO> updateNotificationSent(
            @PathVariable String id,
            @RequestParam Boolean notificationDTO) {
        RecallResponseDTO response = recallService.updateNotificationSent(id, notificationDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/vehicles")
    public ResponseEntity<RecallResponseDTO> assignVehicles(
            @PathVariable String id,
            @RequestParam List<String> vehicleIds) {
        RecallResponseDTO response = recallService.assignVehicles(id, vehicleIds);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/vehicles/{vehicleId}")
    public ResponseEntity<RecallResponseDTO> addVehicle(
            @PathVariable String id,
            @PathVariable String vehicleId) {
        RecallResponseDTO response = recallService.addVehicle(id, vehicleId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/vehicles/{vehicleId}")
    public ResponseEntity<RecallResponseDTO> removeVehicle(
            @PathVariable String id,
            @PathVariable String vehicleId) {
        RecallResponseDTO response = recallService.removeVehicle(id, vehicleId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/vehicle-types")
    public ResponseEntity<RecallResponseDTO> assignVehicleTypes(
            @PathVariable String id,
            @RequestBody List<String> vehicleTypes) {
        RecallResponseDTO response = recallService.assignVehicleTypes(id, vehicleTypes);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/vehicle-types/{vehicleTypeId}")
    public ResponseEntity<RecallResponseDTO> addVehicleType(
            @PathVariable String id,
            @PathVariable String vehicleTypeId) {
        RecallResponseDTO response = recallService.addVehicleType(id, vehicleTypeId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/vehicle-types/{vehicleTypeId}")
    public ResponseEntity<RecallResponseDTO> removeVehicleType(
            @PathVariable String id,
            @PathVariable String vehicleTypeId) {
        RecallResponseDTO response = recallService.removeVehicleType(id, vehicleTypeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/technicians")
    public ResponseEntity<RecallResponseDTO> assignTechnicians(
            @PathVariable String id,
            @RequestBody List<String> technicianIds) {
        RecallResponseDTO response = recallService.assignTechnicians(id, technicianIds);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/technicians/{technicianId}")
    public ResponseEntity<RecallResponseDTO> addTechnician(
            @PathVariable String id,
            @PathVariable String technicianId) {
        RecallResponseDTO response = recallService.addTechnician(id, technicianId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/technicians/{technicianId}")
    public ResponseEntity<RecallResponseDTO> removeTechnician(
            @PathVariable String id,
            @PathVariable String technicianId) {
        RecallResponseDTO response = recallService.removeTechnician(id, technicianId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/reports")
    public ResponseEntity<ReportInfoListDTO> getAllReports(@PathVariable String id) {
        ReportInfoListDTO response = recallService.getAllReports(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/technicians/{technicianId}/recalls")
    public ResponseEntity<List<RecallResponseDTO>> getRecallsByTechnicianId(@PathVariable String technicianId) {
        List<Recall> recalls = recallService.getRecallsByTechnicianId(technicianId);
        List<RecallResponseDTO> response = recalls.stream()
                .map(r -> new RecallResponseDTO(r)) // tuỳ DTO em convert kiểu nào
                .toList();

        return ResponseEntity.ok(response);
    }

}
