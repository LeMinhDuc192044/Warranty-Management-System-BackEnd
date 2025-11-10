package com.warrantyclaim.warrantyclaim_api.service.Implement;


import com.warrantyclaim.warrantyclaim_api.dto.*;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
        } else {
            throw new IllegalStateException("this EVM part type ID " + evmPartId + " does not existed in SC." );
        }
        // Set condition instead of deleting
        sparePartsEVM.setCondition(PartStatus.TRANSFERRED);

        scRepository.save(productsSparePartsSC);
        repository.save(sparePartsEVM);
    }

    @Transactional
    public List<PartsEvmTransferMultipleResponse> transferMultipleEVMPartTypeToSC(Integer quantity, String partTypeId, OfficeBranch officeBranch) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if (partTypeId == null || partTypeId.isEmpty()) {
            throw new IllegalArgumentException("Part Type ID is required");
        }

        if (officeBranch == null) {
            throw new IllegalArgumentException("Office Branch is required");
        }

        List<ProductsSparePartsEVM> availableParts = repository
                .findByPartTypeIdAndConditionIn(
                        partTypeId,
                        List.of(PartStatus.IN_PRODUCTION, PartStatus.ACTIVE)
                );

        if (availableParts.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No available EVM parts found with Part Type ID: " + partTypeId);
        }

        if (availableParts.size() < quantity) {
            throw new IllegalStateException(
                    String.format("Not enough parts available. Requested: %d, Available: %d",
                            quantity, availableParts.size()));
        }

        List<ProductsSparePartsEVM> partsToTransfer = selectRandomParts(availableParts, quantity);

        List<PartsEvmTransferMultipleResponse> responses = new ArrayList<>();

        for (ProductsSparePartsEVM evmPart : partsToTransfer) {
            try {
                // Transfer single part
                transferFromEVMToScOfficeBranch(evmPart.getId(), officeBranch);

                // Create response
                PartsEvmTransferMultipleResponse response = new PartsEvmTransferMultipleResponse();
                response.setId(evmPart.getId());
                response.setName(evmPart.getName());
                response.setVehicleType(evmPart.getVehicleType());
                response.setOldCondition(evmPart.getCondition());
                response.setNewCondition(PartStatus.TRANSFERRED);
                response.setTargetOfficeBranch(officeBranch);
                response.setTransferredAt(LocalDateTime.now());
                response.setSuccess(true);

                responses.add(response);

            } catch (Exception e) {
                // Log error and continue with next part
                PartsEvmTransferMultipleResponse errorResponse = new PartsEvmTransferMultipleResponse();
                errorResponse.setId(evmPart.getId());
                errorResponse.setSuccess(false);
                errorResponse.setErrorMessage(e.getMessage());
                responses.add(errorResponse);
            }
        }
        return responses;
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
    public List<PartTypeCountEVMResponse> countEvmPartByType(String partTypeId) {
        partTypeRepository.findById(partTypeId).
                orElseThrow(() -> new ResourceNotFoundException("No available EVM parts found with Part Type ID: " + partTypeId));

        return repository.countByType(partTypeId);
    }

    @Transactional(readOnly = true)
    public List<PartTypeAndPartStatusCountEVMResponse> countEvmPartByTypeAndCondition(String partTypeId, List<PartStatus> statuses) {
        partTypeRepository.findById(partTypeId).
                orElseThrow(() -> new ResourceNotFoundException("No available EVM parts found with Part Type ID: " + partTypeId));

        return repository.countByTypeAndCondition(partTypeId, statuses);
    }


    @Transactional(readOnly = true)
    public List<ProductsSparePartsEVMResponse> searchProductsByName(String name) {
        List<ProductsSparePartsEVM> productsSparePartsEVMS = repository.findByNameContainingIgnoreCase(name);
        return productsSparePartsEVMS.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
    //-----------------------------------Helper---------------------------------------------------------------------


    @Transactional(readOnly = true)
    public List<ProductsSparePartsEVMResponse> searchProductsByPartTypeId(String partTypeId) {


        List<ProductsSparePartsEVM> products = repository.findByPartTypeId(partTypeId);

        return products.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }






    private List<ProductsSparePartsEVM> selectRandomParts(
            List<ProductsSparePartsEVM> availableParts,
            Integer quantity) {

        // Shuffle the list to randomize selection
        List<ProductsSparePartsEVM> shuffledParts = new ArrayList<>(availableParts);
        Collections.shuffle(shuffledParts);

        // Take first 'quantity' items
        return shuffledParts.stream()
                .limit(quantity)
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
