package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.SCPartTypeDTO;
import com.warrantyclaim.warrantyclaim_api.service.SCPartTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sc-parts/inventory")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SCPartTypeController {

    private final SCPartTypeService partTypeService;

    /**
     * Get all SC part types (inventory) with pagination
     * GET /api/sc-parts/inventory?page=0&size=10&sortBy=partName&sortDir=asc
     * Only SC_STAFF and SC_ADMIN can access
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN')")
    public ResponseEntity<Page<SCPartTypeDTO>> getAllPartTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "partName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SCPartTypeDTO> partTypes = partTypeService.getAllPartTypes(pageable);

        return ResponseEntity.ok(partTypes);
    }

    /**
     * Get all SC part types (no pagination) - for dropdowns
     * GET /api/sc-parts/inventory/all
     */
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN')")
    public ResponseEntity<List<SCPartTypeDTO>> getAllPartTypesNoPagination() {
        List<SCPartTypeDTO> partTypes = partTypeService.getAllPartTypes();
        return ResponseEntity.ok(partTypes);
    }

    /**
     * Get part type by ID
     * GET /api/sc-parts/inventory/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN')")
    public ResponseEntity<SCPartTypeDTO> getPartTypeById(@PathVariable String id) {
        try {
            SCPartTypeDTO partType = partTypeService.getPartTypeById(id);
            return ResponseEntity.ok(partType);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get all unique part categories (part names)
     * GET /api/sc-parts/inventory/categories
     */
    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('SC_STAFF', 'SC_ADMIN')")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = partTypeService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
