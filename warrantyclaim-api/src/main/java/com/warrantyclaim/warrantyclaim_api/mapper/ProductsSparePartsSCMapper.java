package com.warrantyclaim.warrantyclaim_api.mapper;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.*;
import org.springframework.stereotype.Component;

@Component
public class ProductsSparePartsSCMapper {
    public void toEntity(ProductsSparePartsSCRequest request, ProductsSparePartsSC product) {
//        product.setId(request.getId());
        product.setName(request.getName());
        product.setCondition(request.getCondition());
        product.setVehicleType(request.getVehicleType());
        product.setOfficeBranch(request.getOfficeBranch());
    }

    public void updateProduct(ProductsSparePartsSCRequest request, ProductsSparePartsSC product) {
//        if(product.getId() != null) {
//            product.setId(request.getId());
//        }
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

        if(product.getOfficeBranch() != null) {
            product.setOfficeBranch(request.getOfficeBranch());
        }
    }

    public ProductsSparePartsSCResponse toResponse(ProductsSparePartsSC product) {
        ProductsSparePartsSCResponse response = new ProductsSparePartsSCResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setOfficeBranch(product.getOfficeBranch());
        response.setCondition(product.getCondition());
        response.setVehicleType(product.getVehicleType());
        response.setVehicleVinId(toVehicleBasicInfoDTO(product.getElectricVehicle()));
        response.setPartTypeId(toSparePartsTypeSCInfoDTO(product.getPartType()));
        return response;
    }

    public VehicleBasicInfoDTO toVehicleBasicInfoDTO(ElectricVehicle vehicle) {
        if (vehicle == null)
            return null;

        VehicleBasicInfoDTO info = new VehicleBasicInfoDTO();
        info.setVehicleId(vehicle.getId());
        info.setVehicleName(vehicle.getName());
        info.setOwner(vehicle.getOwner());
        info.setPicture(vehicle.getPicture());
        info.setEmail(vehicle.getEmail());
        info.setPhoneNumber(vehicle.getPhoneNumber());

        if (vehicle.getVehicleType() != null) {
            info.setModel(vehicle.getVehicleType().getModelName());
        }

        return info;
    }


    public SparePartsTypeSCInfoDTO toSparePartsTypeSCInfoDTO(ProductsSparePartsTypeSC sparePartsType) {
        if (sparePartsType == null) {
            return null;
        }

        SparePartsTypeSCInfoDTO dto = new SparePartsTypeSCInfoDTO();
        dto.setId(sparePartsType.getId());
        dto.setPartName(sparePartsType.getPartName());
        dto.setManufacturer(sparePartsType.getManufacturer());
        dto.setYearModelYear(sparePartsType.getYearModelYear());
        dto.setPrice(sparePartsType.getPrice());

        return dto;
    }


}
