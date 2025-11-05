package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.EVMPartTypeDTO;
import com.warrantyclaim.warrantyclaim_api.service.EVMPartTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts/inventory")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EVMPartTypeController {

    private final EVMPartTypeService partTypeService;

    @GetMapping
    public ResponseEntity<Page<EVMPartTypeDTO>> getAllPartTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "partName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EVMPartTypeDTO> partTypes = partTypeService.getAllPartTypes(pageable);

        return ResponseEntity.ok(partTypes);
    }

    /**
     * Get all EVM part types (no pagination) - for dropdowns
     * GET /api/parts/inventory/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<EVMPartTypeDTO>> getAllPartTypesNoPagination() {
        List<EVMPartTypeDTO> partTypes = partTypeService.getAllPartTypes();
        return ResponseEntity.ok(partTypes);
    }

    /**
     * Get part type by ID
     * GET /api/parts/inventory/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EVMPartTypeDTO> getPartTypeById(@PathVariable String id) {
        try {
            EVMPartTypeDTO partType = partTypeService.getPartTypeById(id);
            return ResponseEntity.ok(partType);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Update stock status of a part type
     * PATCH /api/parts/inventory/{id}/status?stockStatus=IN_STOCK
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<EVMPartTypeDTO> updateStockStatus(
            @PathVariable String id,
            @RequestParam String stockStatus) {
        try {
            EVMPartTypeDTO updated = partTypeService.updateStockStatus(id, stockStatus);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get all unique part categories (part names)
     * GET /api/parts/inventory/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = partTypeService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
