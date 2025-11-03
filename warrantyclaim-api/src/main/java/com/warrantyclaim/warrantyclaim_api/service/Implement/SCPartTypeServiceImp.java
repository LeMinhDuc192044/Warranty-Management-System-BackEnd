package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.SCPartTypeDTO;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeSC;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsTypeSCRepository;
import com.warrantyclaim.warrantyclaim_api.service.SCPartTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SCPartTypeServiceImp implements SCPartTypeService {

    private final ProductsSparePartsTypeSCRepository partTypeRepository;

    @Override
    public Page<SCPartTypeDTO> getAllPartTypes(Pageable pageable) {
        Page<ProductsSparePartsTypeSC> partTypes = partTypeRepository.findAll(pageable);
        return partTypes.map(this::toDTO);
    }

    @Override
    public List<SCPartTypeDTO> getAllPartTypes() {
        List<ProductsSparePartsTypeSC> partTypes = partTypeRepository.findAll();
        return partTypes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SCPartTypeDTO getPartTypeById(String id) {
        ProductsSparePartsTypeSC partType = partTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part type not found with ID: " + id));
        return toDTO(partType);
    }

    @Override
    public List<String> getAllCategories() {
        return partTypeRepository.findDistinctPartNames();
    }

    /**
     * Convert Entity to DTO
     */
    private SCPartTypeDTO toDTO(ProductsSparePartsTypeSC entity) {
        if (entity == null) {
            return null;
        }

        SCPartTypeDTO dto = new SCPartTypeDTO();
        dto.setId(entity.getId());
        dto.setPartName(entity.getPartName());
        dto.setDescription(entity.getDescription());
        dto.setYearModelYear(entity.getYearModelYear());
        dto.setTotalAmountOfProduct(entity.getTotalAmountOfProduct());
        dto.setPrice(entity.getPrice());
        dto.setManufacturer(entity.getManufacturer());

        return dto;
    }
}
