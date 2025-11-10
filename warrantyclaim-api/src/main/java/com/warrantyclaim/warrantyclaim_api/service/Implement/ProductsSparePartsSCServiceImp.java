package com.warrantyclaim.warrantyclaim_api.service.Implement;


import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicle;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsSC;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeSC;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleType;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.mapper.ProductsSparePartsSCMapper;
import com.warrantyclaim.warrantyclaim_api.repository.*;
import com.warrantyclaim.warrantyclaim_api.service.ProductsSparePartsSCService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsSparePartsSCServiceImp implements ProductsSparePartsSCService {

    private final ProductsSparePartsSCRepository repository;
    private final ProductsSparePartsTypeSCRepository partTypeRepository;
    private final ElectricVehicleRepository vehicleRepository;
    private final ProductsSparePartsSCMapper productsSparePartsSCMapper;


    @Transactional
    public ProductsSparePartsSCResponse createProduct(ProductsSparePartsSCRequest request) {
//        if (repository.existsById(request.getId())) {
//            throw new RuntimeException("Product with ID " + request.getId() + " already exists");
//        }

        ProductsSparePartsSC product = new ProductsSparePartsSC();

        product.setId(generatePartNumber(request.getVehicleType(), request.getPartTypeId()));

        if (request.getPartTypeId() != null) {
            ProductsSparePartsTypeSC partType = partTypeRepository.findById(request.getPartTypeId())
                    .orElse(null);
            product.setPartType(partType);
        }

        if (request.getVehicleVinId() != null) {
            ElectricVehicle vehicle = vehicleRepository.findById(request.getVehicleVinId())
                    .orElse(null);
            product.setElectricVehicle(vehicle);
        }

        product.setId(generatePartNumber(request.getVehicleType(), request.getPartTypeId()));
        productsSparePartsSCMapper.toEntity(request, product);

        product = repository.save(product);
        return productsSparePartsSCMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<PartTypeCountResponse> countPartTypeByOfficeBranch(OfficeBranch officeBranch, String partTypeId) {
        return repository.countByBranchAndPartType(officeBranch, partTypeId);
    }


    @Transactional(readOnly = true)
    public List<ProductsSparePartsSCResponse> getAllProducts() {
        List <ProductsSparePartsSC> productsSparePartsSCS = repository.findAll();
        return productsSparePartsSCS.stream()
                .map(productsSparePartsSCMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SparePartInfoScDTO> getByOfficeBranch(OfficeBranch officeBranch) {
        return repository.findByOfficeBranch(officeBranch);
    }



    @Override
    @Transactional(readOnly = true)
    public ProductsSparePartsSCResponse getProductById(String id) {
        ProductsSparePartsSC product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return productsSparePartsSCMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductsSparePartsSCResponse updateProduct(String id, ProductsSparePartsSCRequest request) {
        ProductsSparePartsSC product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));



        if (request.getPartTypeId() != null) {
            ProductsSparePartsTypeSC partType = partTypeRepository.findById(request.getPartTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Part SC for product is incorrect: " + request.getPartTypeId()));
            product.setPartType(partType);
        }


        if (request.getVehicleVinId() != null) {
            ElectricVehicle vehicle = vehicleRepository.findById(request.getVehicleVinId())
                    .orElseThrow(() -> new ResourceNotFoundException("Electric vehicle for product is incorrect: " + request.getVehicleVinId()));
            product.setElectricVehicle(vehicle);
        }

        productsSparePartsSCMapper.updateProduct(request, product);
        product = repository.save(product);

        return productsSparePartsSCMapper.toResponse(product);
    }



    @Override
    @Transactional
    public ProductsSparePartsSCResponse mapSerialToVehicle(String id, String vehicleId) {
        ProductsSparePartsSC product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if(product.getCondition() != PartStatus.IN_PRODUCTION && product.getCondition() != PartStatus.ACTIVE) {
            throw new IllegalStateException("Part's condition has to be IN_PRODUCTION or ACTIVE to map serial to vin");
        }

        if (vehicleId != null) {
            ElectricVehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Electric vehicle for product is incorrect: " + vehicleId));
            product.setElectricVehicle(vehicle);

            String partTypeId = product.getPartType().getId();
            List<ProductsSparePartsSC> previousParts = repository
                    .findByElectricVehicleAndPartType_Id(vehicle, partTypeId);

            for (ProductsSparePartsSC previousPart : previousParts) {
                if (!previousPart.getId().equals(id)) {
                    previousPart.setCondition(PartStatus.REPLACED);
                    previousPart.setElectricVehicle(null);
                    repository.save(previousPart);
                }
            }

            product.setElectricVehicle(vehicle);
        }



        repository.save(product);
        return productsSparePartsSCMapper.toResponse(product);
    }


    @Override
    public List<ProductsSparePartsSCResponse> searchProductsByName(String name) {
        List <ProductsSparePartsSC> productsSparePartsSCS = repository.findByNameContainingIgnoreCase(name);
        return productsSparePartsSCS.stream()
                .map(productsSparePartsSCMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PartTypeAndPartStatusCountEVMResponse> countEvmPartByTypeAndCondition(String partTypeId, PartStatus statuses) {
        partTypeRepository.findById(partTypeId).
                orElseThrow(() -> new ResourceNotFoundException("No available EVM parts found with Part Type ID: " + partTypeId));

        return repository.countByTypeAndCondition(partTypeId, statuses);
    }

    // DELETE
    @Transactional
    public void deleteProduct(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    private String generatePartNumber(VehicleType model, String partTypeId) {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        String modelCode = autoGenerateModelCode(model);
        String partType = extractGroup(partTypeId);

        return String.join("-", "VF", "HCM", modelCode, partType, uuid);
    }

    public String extractGroup(String partTypeId) {
        if (partTypeId == null || partTypeId.isEmpty()) {
            return "";
        }

        // Split by dash "-"
        String[] parts = partTypeId.split("-");

        // Group is always the first part
        return parts.length > 0 ? parts[0].toUpperCase() : "";
    }


    private String autoGenerateModelCode(VehicleType model) {
        // Example mapping:
        switch (model) {
            case VF3: return "003";
            case VF5: return "005";
            case VF6: return "006";
            case VF7: return "007";
            case VF8: return "008";
            case VF9: return "009";
            default: return "000";
        }
    }
}
