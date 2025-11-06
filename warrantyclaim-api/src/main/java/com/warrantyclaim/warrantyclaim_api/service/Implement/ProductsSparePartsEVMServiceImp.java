package com.warrantyclaim.warrantyclaim_api.service.Implement;


import com.warrantyclaim.warrantyclaim_api.dto.PartTypeCountEVMResponse;
import com.warrantyclaim.warrantyclaim_api.dto.ProductsSparePartsEVMRequest;
import com.warrantyclaim.warrantyclaim_api.dto.ProductsSparePartsEVMResponse;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsEVM;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsSC;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeEVM;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeSC;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleType;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.mapper.ProductsSparePartsEVMMapper;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsEVMRepository;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsSCRepository;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsTypeEVMRepository;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsTypeSCRepository;
import com.warrantyclaim.warrantyclaim_api.service.ProductsSparePartsEVMService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsSparePartsEVMServiceImp implements ProductsSparePartsEVMService {
    private final ProductsSparePartsEVMRepository repository;
    private final ProductsSparePartsTypeEVMRepository partTypeRepository;
    private final ProductsSparePartsTypeSCRepository partsTypeSCRepository;
    private final ProductsSparePartsEVMMapper mapper;
    private final ProductsSparePartsSCRepository scRepository;

    @Transactional
    public ProductsSparePartsEVMResponse createProduct(ProductsSparePartsEVMRequest request) {


        ProductsSparePartsEVM product = new ProductsSparePartsEVM();


        if (request.getPartTypeId() != null) {
            ProductsSparePartsTypeEVM productType =  partTypeRepository.findById(request.getPartTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("No part type with this id" + request.getPartTypeId()));
            product.setPartType(productType);
        }

        mapper.toEntity(request, product);
        product.setId(generatePartNumber(request.getVehicleType(), request.getPartTypeId()));


        product = repository.save(product);
        return mapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductsSparePartsEVMResponse> getAllProducts() {
        List<ProductsSparePartsEVM> productsSparePartsEVMS = repository.findAll();
        return productsSparePartsEVMS.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void transferFromEVMToScOfficeBranch(String evmPartId, OfficeBranch officeBranch) {

        ProductsSparePartsEVM sparePartsEVM = null;
        if(evmPartId != null && !evmPartId.isEmpty()) {
            sparePartsEVM = repository.findById(evmPartId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product SC not found with ID: " + evmPartId));
            if(sparePartsEVM.getCondition() != PartStatus.IN_PRODUCTION && sparePartsEVM.getCondition() != PartStatus.ACTIVE) {
                throw new IllegalStateException("Part EVM's condition has to be IN_PRODUCTION and ACTIVE");
            }
        }

        ProductsSparePartsTypeSC productsSparePartsTypeSC = partsTypeSCRepository.getReferenceById(sparePartsEVM.getPartType().getId());

        ProductsSparePartsSC productsSparePartsSC = mapper.transferScToSC(sparePartsEVM);
        productsSparePartsSC.setPartType(productsSparePartsTypeSC);
        productsSparePartsSC.setOfficeBranch(officeBranch);

        if(scRepository.findById(evmPartId).isPresent()) {
            productsSparePartsSC.setId(generatePartNumber(sparePartsEVM.getVehicleType(), sparePartsEVM.getPartType().getId()));
        }
        // Set condition instead of deleting
        sparePartsEVM.setCondition(PartStatus.TRANSFERRED);

        scRepository.save(productsSparePartsSC);
        repository.save(sparePartsEVM);
    }

    @Transactional(readOnly = true)
    public ProductsSparePartsEVMResponse getProductById(String id) {
        ProductsSparePartsEVM product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        return mapper.toResponse(product);
    }



    // UPDATE
    @Transactional
    public ProductsSparePartsEVMResponse updateProduct(String id, ProductsSparePartsEVMRequest request) {
        ProductsSparePartsEVM product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if (request.getPartTypeId() != null) {
            ProductsSparePartsTypeEVM partType = partTypeRepository.findById(request.getPartTypeId())
                    .orElse(null);
            product.setPartType(partType);
        }

        mapper.updateProductEVM(request, product);
        product = repository.save(product);

        return mapper.toResponse(product);
    }

    // DELETE
    @Transactional
    public void deleteProduct(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PartTypeCountEVMResponse> countEvmPartByType(String partId) {
        return repository.countByType(partId);
    }


    @Transactional(readOnly = true)
    public List<ProductsSparePartsEVMResponse> searchProductsByName(String name) {
        List<ProductsSparePartsEVM> productsSparePartsEVMS = repository.findByNameContainingIgnoreCase(name);
        return productsSparePartsEVMS.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }


    private String generatePartNumber(VehicleType model, String partTypeId) {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        String modelCode = autoGenerateModelCode(model);
        String partType = extractGroup(partTypeId);

        return String.join("-","VF", "HCM", modelCode, partType, uuid);
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
