package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.EVMPartTypeDTO;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeEVM;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsTypeEVMRepository;
import com.warrantyclaim.warrantyclaim_api.service.EVMPartTypeService;
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
public class EVMPartTypeServiceImp implements EVMPartTypeService {

    private final ProductsSparePartsTypeEVMRepository partTypeRepository;

    @Override
    public Page<EVMPartTypeDTO> getAllPartTypes(Pageable pageable) {
        Page<ProductsSparePartsTypeEVM> partTypes = partTypeRepository.findAll(pageable);
        return partTypes.map(this::toDTO);
    }

    @Override
    public List<EVMPartTypeDTO> getAllPartTypes() {
        List<ProductsSparePartsTypeEVM> partTypes = partTypeRepository.findAll();
        return partTypes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EVMPartTypeDTO getPartTypeById(String id) {
        ProductsSparePartsTypeEVM partType = partTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part type not found with ID: " + id));
        return toDTO(partType);
    }

    @Override
    @Transactional
    public EVMPartTypeDTO updateStockStatus(String id, String stockStatus) {
        // Validate stock status
        if (!stockStatus.equals("IN_STOCK") && !stockStatus.equals("LOW_STOCK")
                && !stockStatus.equals("OUT_OF_STOCK")) {
            throw new IllegalArgumentException("Invalid stock status. Must be IN_STOCK, LOW_STOCK, or OUT_OF_STOCK");
        }

        ProductsSparePartsTypeEVM partType = partTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part type not found with ID: " + id));

        partType.setStockStatus(stockStatus);
        ProductsSparePartsTypeEVM updated = partTypeRepository.save(partType);

        return toDTO(updated);
    }

    @Override
    public List<String> getAllCategories() {
        return partTypeRepository.findDistinctPartNames();
    }

    /**
     * Convert Entity to DTO
     */
    private EVMPartTypeDTO toDTO(ProductsSparePartsTypeEVM entity) {
        if (entity == null) {
            return null;
        }

        EVMPartTypeDTO dto = new EVMPartTypeDTO();
        dto.setId(entity.getId());
        dto.setPartName(entity.getPartName());
        dto.setDescription(entity.getDescription());
        dto.setYearModelYear(entity.getYearModelYear());
        dto.setTotalAmountOfProduct(entity.getTotalAmountOfProduct());
        dto.setPrice(entity.getPrice());
        dto.setManufacturer(entity.getManufacturer());
        dto.setStockStatus(entity.getStockStatus());

        return dto;
    }
}
