package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.PartTypeCountResponse;
import com.warrantyclaim.warrantyclaim_api.dto.ProductsSparePartsSCRequest;
import com.warrantyclaim.warrantyclaim_api.dto.ProductsSparePartsSCResponse;
import com.warrantyclaim.warrantyclaim_api.dto.SparePartInfoScDTO;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsSC;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;

import java.util.List;

public interface ProductsSparePartsSCService {
    public ProductsSparePartsSCResponse createProduct(ProductsSparePartsSCRequest request);
    public List<ProductsSparePartsSCResponse> getAllProducts();
    public ProductsSparePartsSCResponse getProductById(String id);
    public ProductsSparePartsSCResponse updateProduct(String id, ProductsSparePartsSCRequest request);
    public List<ProductsSparePartsSCResponse> searchProductsByName(String name);
    public ProductsSparePartsSCResponse mapSerialToVehicle(String id, String vehicleId);
    public void deleteProduct(String id);
    public List<PartTypeCountResponse> countPartTypeByOfficeBranch(OfficeBranch officeBranch, String partTypeId);
    public List<SparePartInfoScDTO> getByOfficeBranch(OfficeBranch officeBranch);
}
