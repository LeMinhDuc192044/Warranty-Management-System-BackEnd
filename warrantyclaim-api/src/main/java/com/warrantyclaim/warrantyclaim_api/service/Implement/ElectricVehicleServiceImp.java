package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicle;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicleType;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.mapper.ElectricVehicleMapper;
import com.warrantyclaim.warrantyclaim_api.repository.ElectricVehicleRepository;
import com.warrantyclaim.warrantyclaim_api.repository.ElectricVehicleTypeRepository;
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


@Service
@Transactional
@RequiredArgsConstructor
public class ElectricVehicleServiceImp implements ElectricVehicleService {
    private final ElectricVehicleRepository electricVehicleRepository;
    private final ElectricVehicleMapper mapper;
    private final ImageUploadServiceImp imageUploadServiceImp;
    private final ElectricVehicleTypeRepository electricVehicleTypeRepository;

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

}
