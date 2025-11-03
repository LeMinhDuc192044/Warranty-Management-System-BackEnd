package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.SCPartTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SCPartTypeService {
    /**
     * Get all SC part types (with pagination)
     */
    Page<SCPartTypeDTO> getAllPartTypes(Pageable pageable);

    /**
     * Get all SC part types (without pagination)
     */
    List<SCPartTypeDTO> getAllPartTypes();

    /**
     * Get part type by ID
     */
    SCPartTypeDTO getPartTypeById(String id);

    /**
     * Get all unique part categories (part names)
     */
    List<String> getAllCategories();
}
