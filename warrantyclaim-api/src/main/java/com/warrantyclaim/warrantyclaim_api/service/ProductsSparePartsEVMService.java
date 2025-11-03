package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.PartTypeCountEVMResponse;
import com.warrantyclaim.warrantyclaim_api.dto.ProductsSparePartsEVMRequest;
import com.warrantyclaim.warrantyclaim_api.dto.ProductsSparePartsEVMResponse;

import java.util.List;

public interface ProductsSparePartsEVMService {
    public ProductsSparePartsEVMResponse createProduct(ProductsSparePartsEVMRequest request);

    public List<ProductsSparePartsEVMResponse> getAllProducts();

    public ProductsSparePartsEVMResponse getProductById(String id);

    public ProductsSparePartsEVMResponse updateProduct(String id, ProductsSparePartsEVMRequest request);

    public void deleteProduct(String id);

    public List<PartTypeCountEVMResponse> countEvmPartByType(String partId);

    public List<ProductsSparePartsEVMResponse> searchProductsByName(String name);
    public void transferFromScOfficeBranchToEVM(String partScId);
}
