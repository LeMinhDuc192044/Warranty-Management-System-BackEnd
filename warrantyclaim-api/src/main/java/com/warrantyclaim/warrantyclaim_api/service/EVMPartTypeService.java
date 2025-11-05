package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.EVMPartTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EVMPartTypeService {
    /**
     * Get all EVM part types (with pagination)
     */
    Page<EVMPartTypeDTO> getAllPartTypes(Pageable pageable);

    /**
     * Get all EVM part types (without pagination)
     */
    List<EVMPartTypeDTO> getAllPartTypes();

    /**
     * Get part type by ID
     */
    EVMPartTypeDTO getPartTypeById(String id);

    /**
     * Update stock status of a part type
     */
    EVMPartTypeDTO updateStockStatus(String id, String stockStatus);

    /**
     * Get all unique part categories (part names)
     */
    List<String> getAllCategories();
}
