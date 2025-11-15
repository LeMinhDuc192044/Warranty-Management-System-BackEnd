package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicle;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicleType;
import com.warrantyclaim.warrantyclaim_api.entity.WarrantyPolicy;
import com.warrantyclaim.warrantyclaim_api.entity.WarrantyPolicyElectricVehicleType;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.mapper.ElectricVehicleMapper;
import com.warrantyclaim.warrantyclaim_api.repository.ElectricVehicleRepository;
import com.warrantyclaim.warrantyclaim_api.repository.ElectricVehicleTypeRepository;
import com.warrantyclaim.warrantyclaim_api.repository.WarrantyPolicyElectricVehicleTypeRepository;
import com.warrantyclaim.warrantyclaim_api.service.ElectricVehicleService;
import com.warrantyclaim.warrantyclaim_api.utils.VinUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ElectricVehicleServiceImp implements ElectricVehicleService {
    private final ElectricVehicleRepository electricVehicleRepository;
    private final ElectricVehicleMapper mapper;
    private final ImageUploadServiceImp imageUploadServiceImp;
    private final ElectricVehicleTypeRepository electricVehicleTypeRepository;
    private final WarrantyPolicyElectricVehicleTypeRepository warrantyPolicyEVTRepository;


    @Override
    @Transactional
    public VehicleDetailInfo addElectricVehicle(VehicleCreateDTO vehicleCreateDTO, MultipartFile urlPicture) throws IOException {
        //  Kiểm tra VIN hợp lệ
        String vin = vehicleCreateDTO.getVehicleId();
        if (!VinUtils.isValidVin(vin)) {
            throw new IllegalArgumentException("Số VIN không hợp lệ. Định dạng đúng: VF[Model][Year][H|T][Serial] (17 ký tự)");
        }
        if (electricVehicleRepository.existsById(vin)) {
            throw new IllegalArgumentException("Số VIN đã tồn tại trong hệ thống");
        }

//        ElectricVehicleType electricVehicleType = electricVehicleTypeRepository.findById(vehicleCreateDTO.getElectricVehicleTypeId())
//                .orElseThrow(() -> new ResourceNotFoundException("No vehicle type with this Id"));

        ElectricVehicle electricVehicle = mapper.toEntityElectricVehicle(vehicleCreateDTO);

        if (vehicleCreateDTO.getElectricVehicleTypeId() != null) {
            ElectricVehicleType vehicleType = electricVehicleTypeRepository
                    .findById(vehicleCreateDTO.getElectricVehicleTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found"));
            electricVehicle.setVehicleType(vehicleType);
        }

        if(urlPicture != null && !urlPicture.isEmpty()) {
            String imageUrl = imageUploadServiceImp.uploadImage(urlPicture);
            electricVehicle.setPicture(imageUrl);
        }

        if(vehicleCreateDTO.getStatus() != null) {
            electricVehicle.setStatus(vehicleCreateDTO.getStatus());
        } else {
            electricVehicle.setStatus(VehicleStatus.ACTIVE);
        }

        electricVehicleRepository.save(electricVehicle);

        return mapper.toVehicleDetailInfo(electricVehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public ElectricVehicleResponseDTO getVehicleById(String id) {
        ElectricVehicle vehicle = electricVehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Electric Vehicle not found with ID: " + id));

        return mapper.toResponseDTO(vehicle);
    }

    /**
     * Update vehicle
     */
    @Override
    @Transactional
    public ElectricVehicleResponseDTO updateVehicle(String id, ElectricVehicleUpdateRequestDTO request, MultipartFile urlPicture) {
        // 1. Find vehicle
        ElectricVehicle vehicle = electricVehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Electric Vehicle not found with ID: " + id));

        // 2. Validate and fetch vehicle type if changed
        if (request.getVehicleTypeId() != null) {
            ElectricVehicleType vehicleType = electricVehicleTypeRepository.findById(request.getVehicleTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle Type not found with ID: " + request.getVehicleTypeId()));
            vehicle.setVehicleType(vehicleType);
        }


        if (urlPicture != null && !urlPicture.isEmpty()) {
            try {
                // Delete old image if exists
                if (vehicle.getPicture() != null && !vehicle.getPicture().isEmpty()) {
                    try {
                        imageUploadServiceImp.deleteImage(vehicle.getPicture());
                    } catch (IOException e) {
                        // Log but don't fail the update if old image deletion fails
                        System.err.println("Failed to delete old image: " + e.getMessage());
                    }
                }

                // Upload new image
                String imageUrl = imageUploadServiceImp.uploadImage(urlPicture);
                vehicle.setPicture(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
            }
        }

        // 3. Update fields
        mapper.updateEntityElectricVehicle(request, vehicle);
        // 4. Save
        ElectricVehicle updatedVehicle = electricVehicleRepository.save(vehicle);

        return mapper.toResponseDTO(updatedVehicle);
    }

    @Override
    @Transactional
    public ElectricVehicleResponseDTO updateReturnDate(String id, LocalDate returnDate) {
        ElectricVehicle vehicle = electricVehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Electric Vehicle not found with ID: " + id));

        if(vehicle.getPurchaseDate().isBefore(returnDate)) {
            throw new RuntimeException("Return date has to be after purchaseDate");
        }

        vehicle.setReturnDate(returnDate);
        vehicle = electricVehicleRepository.save(vehicle);
        return mapper.toResponseDTO(vehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(String id) {
        // 1. Check if vehicle exists
        ElectricVehicle vehicle = electricVehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Electric Vehicle not found with ID: " + id));


        if (vehicle.getPicture() != null && !vehicle.getPicture().isEmpty()) {
            try {
                imageUploadServiceImp.deleteImage(vehicle.getPicture());
            } catch (IOException e) {
                // Log but don't fail the deletion if image deletion fails
                System.err.println("Failed to delete image: " + e.getMessage());
            }
        }

        // 2. Delete
        electricVehicleRepository.deleteById(id);
    }
    @Override
    @Transactional
    public Page<ElectricVehicleListResponseDTO> getAllVehicles(Pageable pageable) {
        Page<ElectricVehicle> vehicles = electricVehicleRepository.findAll(pageable);
        return vehicles.map(mapper::toListResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarrantyStatusDTO> getWarrantyStatus(String vin) {
        ElectricVehicle vehicle = electricVehicleRepository.findById(vin)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe với VIN: " + vin));

        LocalDate purchaseDate = vehicle.getPurchaseDate();
        ElectricVehicleType type = vehicle.getVehicleType();

        if (purchaseDate == null || type == null) {
            throw new IllegalStateException("Xe chưa có đủ thông tin để kiểm tra bảo hành");
        }

        List<WarrantyStatusDTO> result = new ArrayList<>();

        for (WarrantyPolicyElectricVehicleType link : type.getWarrantyPolicyElectricVehicleTypes()) {
            WarrantyPolicy policy = link.getWarrantyPolicy();
            LocalDate endDate = purchaseDate.plusMonths(policy.getCoverageDurationMonths());
            boolean isValid = LocalDate.now().isBefore(endDate);

            WarrantyStatusDTO dto = new WarrantyStatusDTO();
            dto.setPolicyName(policy.getName());
            dto.setCoverageType(policy.getCoverageTypeWarrantyPolicy().name());
            dto.setWarrantyEndDate(endDate);
            dto.setUnderWarranty(isValid);


            result.add(dto);
        }

        return result;
    }

// xem danh sach xe con bao hanh
    public List<VehicleWarrantyStatusDTO> getVehiclesUnderWarranty() {
        List<ElectricVehicle> vehicles = electricVehicleRepository.findAll();
        List<VehicleWarrantyStatusDTO> result = new ArrayList<>();

        for (ElectricVehicle ev : vehicles) {
            List<WarrantyPolicyElectricVehicleType> links =
                    warrantyPolicyEVTRepository.findByVehicleType(ev.getVehicleType());

            for (WarrantyPolicyElectricVehicleType link : links) {
                WarrantyPolicy policy = link.getWarrantyPolicy();
                LocalDate endDate = ev.getPurchaseDate().plusMonths(policy.getCoverageDurationMonths());
                if (!LocalDate.now().isAfter(endDate)) {
                    result.add(new VehicleWarrantyStatusDTO(
                            ev.getId(),
                            ev.getName(),
                            ev.getPurchaseDate(),
                            endDate,
                            true
                    ));
                    break;
                }
            }
        }

        return result;
    }
    @Override
    @Transactional
    public UpdateVersionDTO updateVersion(String vehicleId, String newVersion) {
        ElectricVehicle vehicle = electricVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));

        vehicle.setVersion(newVersion);
        ElectricVehicle updatedVehicle = electricVehicleRepository.save(vehicle);

        // Convert to DTO
        return new UpdateVersionDTO(
                updatedVehicle.getId(),
                updatedVehicle.getVersion()
        );
    }

}
