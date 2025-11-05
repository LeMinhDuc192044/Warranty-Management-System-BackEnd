package com.warrantyclaim.warrantyclaim_api.mapper;

import com.warrantyclaim.warrantyclaim_api.dto.ProductsSparePartsEVMRequest;
import com.warrantyclaim.warrantyclaim_api.dto.ProductsSparePartsEVMResponse;
import com.warrantyclaim.warrantyclaim_api.dto.SparePartsTypeEVMInfoDTO;
import com.warrantyclaim.warrantyclaim_api.dto.SparePartsTypeSCInfoDTO;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsEVM;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsSC;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeEVM;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeSC;
import org.springframework.stereotype.Component;

@Component
public class ProductsSparePartsEVMMapper {

    public void toEntity(ProductsSparePartsEVMRequest request, ProductsSparePartsEVM product) {
        product.setName(request.getName());
        product.setCondition(request.getCondition());
        product.setVehicleType(request.getVehicleType());
    }

    public void updateProductEVM(ProductsSparePartsEVMRequest request, ProductsSparePartsEVM product) {
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getCondition() != null) {
            product.setCondition(request.getCondition());
        }

        if(product.getVehicleType() != null) {
            product.setVehicleType(request.getVehicleType());
        }
    }


    public ProductsSparePartsSC transferScToSC(ProductsSparePartsEVM productsSparePartsEVM) {
        if(productsSparePartsEVM == null) {
            return null;
        }

        ProductsSparePartsSC productsSparePartsSC = new ProductsSparePartsSC();
        productsSparePartsSC.setId(productsSparePartsEVM.getId());
        productsSparePartsSC.setName(productsSparePartsEVM.getName());
        productsSparePartsSC.setCondition(productsSparePartsEVM.getCondition());
        productsSparePartsSC.setVehicleType(productsSparePartsEVM.getVehicleType());
        // Transfer type in service layer

        return productsSparePartsSC;
    }

    public ProductsSparePartsEVMResponse toResponse(ProductsSparePartsEVM product) {
        ProductsSparePartsEVMResponse response = new ProductsSparePartsEVMResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setCondition(product.getCondition());
        response.setVehicleType(product.getVehicleType());
        response.setPartTypeInfoDTO(toSparePartsTypeEVMInfoDTO(product.getPartType()));
        return response;
    }

    public SparePartsTypeEVMInfoDTO toSparePartsTypeEVMInfoDTO(ProductsSparePartsTypeEVM sparePartsType) {
        if (sparePartsType == null) {
            return null;
        }

        SparePartsTypeEVMInfoDTO dto = new SparePartsTypeEVMInfoDTO();
        dto.setId(sparePartsType.getId());
        dto.setPartName(sparePartsType.getPartName());
        dto.setManufacturer(sparePartsType.getManufacturer());
        dto.setYearModelYear(sparePartsType.getYearModelYear());
        dto.setPrice(sparePartsType.getPrice());

        return dto;
    }
}
