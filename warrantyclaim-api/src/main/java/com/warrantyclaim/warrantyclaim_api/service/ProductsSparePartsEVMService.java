package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;

import java.util.List;

public interface ProductsSparePartsEVMService {
    public ProductsSparePartsEVMResponse createProduct(ProductsSparePartsEVMRequest request);

    public List<ProductsSparePartsEVMResponse> getAllProducts();

    public ProductsSparePartsEVMResponse getProductById(String id);

    public ProductsSparePartsEVMResponse updateProduct(String id, ProductsSparePartsEVMRequest request);

    public void deleteProduct(String id);

    public List<PartTypeCountEVMResponse> countEvmPartByType(String partId);

    public List<ProductsSparePartsEVMResponse> searchProductsByName(String name);
    public void transferFromEVMToScOfficeBranch(String partScId, OfficeBranch officeBranch);
    public List<PartsEvmTransferMultipleResponse> transferMultipleEVMPartTypeToSC(Integer quantity, String partTypeId, OfficeBranch officeBranch);
    public List<PartTypeAndPartStatusCountEVMResponse> countEvmPartByTypeAndCondition(String partTypeId, PartStatus statuses);
}
