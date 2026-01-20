package com.warrantyclaim.warrantyclaim_api.mapper;

import com.warrantyclaim.warrantyclaim_api.dto.ElectricVehicleTypeResponseDTO;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicleType;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
@Component  // ✅ PHẢI CÓ
public class ElectricVehicleTypeMapper {
    public ElectricVehicleTypeResponseDTO toResponseDTO(ElectricVehicleType entity) {
        if (entity == null) {
            return null;
        }

        ElectricVehicleTypeResponseDTO dto = new ElectricVehicleTypeResponseDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setModelName(entity.getModelName());
        dto.setYearModelYear(entity.getYearModelYear());
        dto.setBatteryType(entity.getBatteryType());
        dto.setPrice(entity.getPrice());

        dto.setQuantity(entity.getQuantity());

        return dto;
    }

    /**
     * Convert List<Entity> -> List<ResponseDTO>
     */
    public List<ElectricVehicleTypeResponseDTO> toResponseDTOList(List<ElectricVehicleType> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
