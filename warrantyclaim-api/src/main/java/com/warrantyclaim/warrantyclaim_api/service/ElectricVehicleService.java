package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ElectricVehicleService {
    public VehicleDetailInfo addElectricVehicle(VehicleCreateDTO vehicleCreateDTO, MultipartFile urlPicture) throws IOException;

    public ElectricVehicleResponseDTO updateVehicle(String id, ElectricVehicleUpdateRequestDTO request, MultipartFile urlPicture);

    public ElectricVehicleResponseDTO getVehicleById(String id);

    public void deleteVehicle(String id);

    public Page<ElectricVehicleListResponseDTO> getAllVehicles(Pageable pageable);
    public ElectricVehicleResponseDTO updateReturnDate(String id, LocalDate returnDate);

    List<WarrantyStatusDTO> getWarrantyStatus(String vin);

    public List<VehicleWarrantyStatusDTO> getVehiclesUnderWarranty();


}
