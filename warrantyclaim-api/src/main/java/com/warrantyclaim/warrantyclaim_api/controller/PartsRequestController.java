package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.PartsRequestCreateDTO;
import com.warrantyclaim.warrantyclaim_api.dto.PartsRequestResponseDTO;
import com.warrantyclaim.warrantyclaim_api.dto.PartsRequestUpdateDTO;
import com.warrantyclaim.warrantyclaim_api.dto.PartsResponseListDTO;
import com.warrantyclaim.warrantyclaim_api.service.PartsRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parts-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PartsRequestController {

    private final PartsRequestService partsRequestService;

    /**
     * Create parts request - Only SC_ADMIN can create
     * POST /api/parts-requests
     */
    @PostMapping
    @PreAuthorize("hasRole('SC_ADMIN')")
    public ResponseEntity<PartsRequestResponseDTO> createPartsRequest(
            @Valid @RequestBody PartsRequestCreateDTO request) {
        PartsRequestResponseDTO response = partsRequestService.createPartsRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get parts request by ID
     * SC_STAFF can view, SC_ADMIN can view, EVM_STAFF can view
     * GET /api/parts-requests/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN', 'EVM_STAFF', 'EVM_ADMIN')")
    public ResponseEntity<PartsRequestResponseDTO> getPartsRequestById(@PathVariable String id) {
        PartsRequestResponseDTO response = partsRequestService.getPartsRequestById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all parts requests (paginated)
     * SC_STAFF can view inventory, SC_ADMIN can view, EVM_STAFF can view
     * GET /api/parts-requests?page=0&size=10
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN', 'EVM_STAFF', 'EVM_ADMIN')")
    public ResponseEntity<Page<PartsResponseListDTO>> getAllPartsRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "requestDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PartsResponseListDTO> response = partsRequestService.getAllPartsRequests(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Update parts request - Only SC_ADMIN can update
     * PUT /api/parts-requests/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SC_ADMIN')")
    public ResponseEntity<PartsRequestResponseDTO> updatePartsRequest(
            @PathVariable String id,
            @Valid @RequestBody PartsRequestUpdateDTO request) {
        PartsRequestResponseDTO response = partsRequestService.updatePartsRequest(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete parts request - Only SC_ADMIN can delete
     * DELETE /api/parts-requests/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SC_ADMIN')")
    public ResponseEntity<Void> deletePartsRequest(@PathVariable String id) {
        partsRequestService.deletePartsRequest(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Approve parts request - Only EVM_STAFF and EVM_ADMIN can approve
     * PATCH /api/parts-requests/{id}/approve
     */
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('EVM_STAFF', 'EVM_ADMIN')")
    public ResponseEntity<?> approvePartsRequest(@PathVariable String id) {
        try {
            PartsRequestResponseDTO response = partsRequestService.approvePartsRequest(id);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            // Insufficient stock or other validation error
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error approving request: " + e.getMessage());
        }
    }

    /**
     * Reject parts request - Only EVM_STAFF and EVM_ADMIN can reject
     * PATCH /api/parts-requests/{id}/reject
     */
    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('EVM_STAFF', 'EVM_ADMIN')")
    public ResponseEntity<?> rejectPartsRequest(
            @PathVariable String id,
            @RequestParam(required = false) String reason) {
        try {
            PartsRequestResponseDTO response = partsRequestService.rejectPartsRequest(id, reason);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error rejecting request: " + e.getMessage());
        }
    }
}
