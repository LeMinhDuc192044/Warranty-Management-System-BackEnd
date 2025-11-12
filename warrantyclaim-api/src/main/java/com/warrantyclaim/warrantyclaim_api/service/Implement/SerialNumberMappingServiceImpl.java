package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.SerialNumberMappingRequestDTO;
import com.warrantyclaim.warrantyclaim_api.dto.SerialNumberMappingResponseDTO;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsSC;
import com.warrantyclaim.warrantyclaim_api.entity.SCTechnician;
import com.warrantyclaim.warrantyclaim_api.entity.SerialNumberMapping;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsSCRepository;
import com.warrantyclaim.warrantyclaim_api.repository.SCTechnicianRepository;
import com.warrantyclaim.warrantyclaim_api.repository.SerialNumberMappingRepository;
import com.warrantyclaim.warrantyclaim_api.service.SerialNumberMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SerialNumberMappingServiceImpl implements SerialNumberMappingService {

    private final SerialNumberMappingRepository serialNumberMappingRepository;
    private final ProductsSparePartsSCRepository productsSCRepository;
    private final SCTechnicianRepository scTechnicianRepository;

    @Override
    @Transactional
    public SerialNumberMappingResponseDTO mapSerialToVehicle(SerialNumberMappingRequestDTO requestDTO) {
        // Validate serial number chưa được sử dụng
        if (serialNumberMappingRepository.existsBySerialNumber(requestDTO.getSerialNumber())) {
            throw new IllegalStateException("Serial number already mapped: " + requestDTO.getSerialNumber());
        }

        // Validate durability percentage
        if (requestDTO.getDurabilityPercentage() != null) {
            if (requestDTO.getDurabilityPercentage() < 0 || requestDTO.getDurabilityPercentage() > 100) {
                throw new IllegalArgumentException("Durability percentage must be between 0 and 100");
            }
        }

        // Tạo mapping entity
        SerialNumberMapping mapping = new SerialNumberMapping();
        mapping.setSerialNumber(requestDTO.getSerialNumber());
        mapping.setPartId(requestDTO.getPartId());
        mapping.setVehicleVIN(requestDTO.getVehicleVIN());
        mapping.setClaimId(requestDTO.getClaimId());
        mapping.setMappingDate(LocalDateTime.now());
        mapping.setMappedByTechnicianId(requestDTO.getMappedByTechnicianId());
        mapping.setNotes(requestDTO.getNotes());
        mapping.setDurabilityPercentage(requestDTO.getDurabilityPercentage());

        // Save
        SerialNumberMapping savedMapping = serialNumberMappingRepository.save(mapping);

        // Convert to DTO
        return convertToResponseDTO(savedMapping);
    }

    @Override
    public List<SerialNumberMappingResponseDTO> getSerialNumbersByVIN(String vin) {
        List<SerialNumberMapping> mappings = serialNumberMappingRepository.findByVehicleVIN(vin);
        return mappings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SerialNumberMappingResponseDTO> getSerialNumbersByClaim(String claimId) {
        List<SerialNumberMapping> mappings = serialNumberMappingRepository.findByClaimIdWithDetails(claimId);
        return mappings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isSerialNumberUsed(String serialNumber) {
        return serialNumberMappingRepository.existsBySerialNumber(serialNumber);
    }

    @Override
    public SerialNumberMappingResponseDTO getSerialNumberDetails(String serialNumber) {
        SerialNumberMapping mapping = serialNumberMappingRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Serial number not found: " + serialNumber));

        return convertToResponseDTO(mapping);
    }

    @Override
    @Transactional
    public void deleteMapping(String serialNumber) {
        if (!serialNumberMappingRepository.existsBySerialNumber(serialNumber)) {
            throw new ResourceNotFoundException("Serial number not found: " + serialNumber);
        }
        serialNumberMappingRepository.deleteById(serialNumber);
    }

    @Override
    @Transactional
    public SerialNumberMappingResponseDTO updateDurability(String serialNumber, Integer durabilityPercentage) {
        SerialNumberMapping mapping = serialNumberMappingRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Serial number not found: " + serialNumber));

        // Validate durability
        if (durabilityPercentage < 0 || durabilityPercentage > 100) {
            throw new IllegalArgumentException("Durability percentage must be between 0 and 100");
        }

        mapping.setDurabilityPercentage(durabilityPercentage);
        SerialNumberMapping updated = serialNumberMappingRepository.save(mapping);

        return convertToResponseDTO(updated);
    }

    /**
     * Convert entity to DTO
     */
    private SerialNumberMappingResponseDTO convertToResponseDTO(SerialNumberMapping mapping) {
        SerialNumberMappingResponseDTO dto = new SerialNumberMappingResponseDTO();
        dto.setSerialNumber(mapping.getSerialNumber());
        dto.setPartId(mapping.getPartId());
        dto.setVehicleVIN(mapping.getVehicleVIN());
        dto.setClaimId(mapping.getClaimId());
        dto.setMappingDate(mapping.getMappingDate());
        dto.setMappedByTechnicianId(mapping.getMappedByTechnicianId());
        dto.setNotes(mapping.getNotes());
        dto.setDurabilityPercentage(mapping.getDurabilityPercentage());

        // Get part name if available
        if (mapping.getSparePart() != null) {
            dto.setPartName(mapping.getSparePart().getName());
        } else if (mapping.getPartId() != null) {
            // Fallback: query part name
            productsSCRepository.findById(mapping.getPartId())
                    .ifPresent(part -> dto.setPartName(part.getName()));
        }

        // Get technician name if available
        if (mapping.getMappedByTechnicianId() != null) {
            scTechnicianRepository.findById(mapping.getMappedByTechnicianId())
                    .ifPresent(tech -> dto.setTechnicianName(tech.getName()));
        }

        return dto;
    }
}
