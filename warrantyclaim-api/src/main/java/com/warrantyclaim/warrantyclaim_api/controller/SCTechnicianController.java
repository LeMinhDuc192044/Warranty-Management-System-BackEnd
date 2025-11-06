package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.service.SCTechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/sc-technicians")
public class SCTechnicianController {

    @Autowired
    private SCTechnicianService technicianService;

    @PostMapping
    public ResponseEntity<SCTechnicianResponseDTO> createTechnician(
            @RequestBody SCTechnicianCreateDTO createDTO) {
        SCTechnicianResponseDTO response = technicianService.createTechnician(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SCTechnicianResponseDTO> getTechnicianById(@PathVariable String id) {
        SCTechnicianResponseDTO response = technicianService.getTechnicianById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SCTechnicianResponseDTO> updateTechnician(
            @PathVariable String id,
            @RequestBody SCTechnicianUpdateDTO updateDTO) {
        SCTechnicianResponseDTO response = technicianService.updateTechnician(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnician(@PathVariable String id) {
        technicianService.deleteTechnician(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<SCTechnicianListDTO>> getAllTechnicians(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SCTechnicianListDTO> technicians = technicianService.getAllTechnicians(pageable);
        return ResponseEntity.ok(technicians);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SCTechnicianResponseDTO> getTechnicianByUserId(@PathVariable Long userId) {
        SCTechnicianResponseDTO technician = technicianService.getTechnicianByUserId(userId);
        return ResponseEntity.ok(technician);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<SCTechnicianListDTO>> searchTechnicians(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SCTechnicianListDTO> technicians = technicianService.searchTechnicians(keyword, pageable);
        return ResponseEntity.ok(technicians);
    }

    @GetMapping("/branch/{branchOffice}")
    public ResponseEntity<Page<SCTechnicianListDTO>> getTechniciansByBranch(
            @PathVariable String branchOffice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<SCTechnicianListDTO> technicians = technicianService.getTechniciansByBranch(branchOffice, pageable);
        return ResponseEntity.ok(technicians);
    }

    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<Page<SCTechnicianListDTO>> getTechniciansBySpecialty(
            @PathVariable String specialty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<SCTechnicianListDTO> technicians = technicianService.getTechniciansBySpecialty(specialty, pageable);
        return ResponseEntity.ok(technicians);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<SCTechnicianResponseDTO> updatePassword(
            @PathVariable String id,
            @RequestParam String newPassword) {
        SCTechnicianResponseDTO response = technicianService.updatePassword(id, newPassword);
        return ResponseEntity.ok(response);
    }
}