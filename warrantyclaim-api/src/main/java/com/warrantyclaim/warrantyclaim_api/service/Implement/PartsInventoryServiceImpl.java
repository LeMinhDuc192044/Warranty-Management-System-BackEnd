package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.PartAvailabilityItemDTO;
import com.warrantyclaim.warrantyclaim_api.dto.PartTypeCountResponse;
import com.warrantyclaim.warrantyclaim_api.dto.PartsAvailabilityResponseDTO;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeSC;
import com.warrantyclaim.warrantyclaim_api.entity.WarrantyClaim;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsSCRepository;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsTypeSCRepository;
import com.warrantyclaim.warrantyclaim_api.repository.WarrantyClaimRepository;
import com.warrantyclaim.warrantyclaim_api.service.PartsInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartsInventoryServiceImpl implements PartsInventoryService {

    private final ProductsSparePartsSCRepository partsSCRepository;
    private final ProductsSparePartsTypeSCRepository partsTypeSCRepository;
    private final WarrantyClaimRepository warrantyClaimRepository;

    @Override
    public PartsAvailabilityResponseDTO checkPartsAvailability(String claimId) {
        // Lấy thông tin warranty claim
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found with ID: " + claimId));

        // Parse required parts từ claim (format: "partType1,partType2,partType3")
        List<String> partTypeIds = parseRequiredParts(claim.getRequiredParts());

        // Lấy office branch từ claim
        OfficeBranch officeBranch = claim.getOfficeBranch();

        if (officeBranch == null) {
            throw new IllegalStateException("Office branch not specified for claim: " + claimId);
        }

        // Kiểm tra tình trạng từng phụ tùng
        List<PartAvailabilityItemDTO> partItems = checkPartTypeAvailability(partTypeIds, officeBranch.name());

        // Kiểm tra xem tất cả phụ tùng có available không
        boolean allAvailable = partItems.stream().allMatch(PartAvailabilityItemDTO::getIsAvailable);

        // Tạo response
        PartsAvailabilityResponseDTO response = new PartsAvailabilityResponseDTO();
        response.setClaimId(claimId);
        response.setOfficeBranch(officeBranch.name());
        response.setParts(partItems);
        response.setAllPartsAvailable(allAvailable);

        if (allAvailable) {
            response.setOverallMessage("Tất cả phụ tùng đều có sẵn");
        } else {
            long unavailableCount = partItems.stream()
                    .filter(p -> !p.getIsAvailable())
                    .count();
            response.setOverallMessage(unavailableCount + " phụ tùng không có sẵn");
        }

        return response;
    }

    @Override
    public List<PartAvailabilityItemDTO> checkPartTypeAvailability(List<String> partTypeIds, String officeBranch) {
        List<PartAvailabilityItemDTO> result = new ArrayList<>();

        OfficeBranch branch;
        try {
            branch = OfficeBranch.valueOf(officeBranch.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid office branch: " + officeBranch);
        }

        for (String partTypeId : partTypeIds) {
            // Lấy thông tin loại phụ tùng
            ProductsSparePartsTypeSC partType = partsTypeSCRepository.findById(partTypeId)
                    .orElse(null);

            if (partType == null) {
                // Part type không tồn tại
                PartAvailabilityItemDTO item = new PartAvailabilityItemDTO();
                item.setPartTypeId(partTypeId);
                item.setPartName("Unknown Part");
                item.setAvailableQuantity(0);
                item.setIsAvailable(false);
                item.setMessage("Loại phụ tùng không tồn tại");
                result.add(item);
                continue;
            }

            // Đếm số lượng available
            Integer availableQty = getAvailableQuantity(partTypeId, officeBranch);

            // Tạo item response
            PartAvailabilityItemDTO item = new PartAvailabilityItemDTO();
            item.setPartTypeId(partTypeId);
            item.setPartName(partType.getPartName());
            item.setAvailableQuantity(availableQty);
            item.setIsAvailable(availableQty > 0);

            if (availableQty > 0) {
                item.setMessage("Có sẵn " + availableQty + " phụ tùng");
            } else {
                item.setMessage("Hết hàng - cần đặt hàng từ EVM");
            }

            result.add(item);
        }

        return result;
    }

    @Override
    public Integer getAvailableQuantity(String partTypeId, String officeBranch) {
        try {
            OfficeBranch branch = OfficeBranch.valueOf(officeBranch.toUpperCase());

            // Sử dụng query count từ repository
            List<PartTypeCountResponse> counts = partsSCRepository.countByBranchAndPartType(branch, partTypeId);

            if (counts.isEmpty()) {
                return 0;
            }

            return counts.get(0).count().intValue();

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid office branch: " + officeBranch);
        }
    }

    /**
     * Parse chuỗi required parts thành list part type IDs
     * Format: "PART-001,PART-002,PART-003"
     */
    private List<String> parseRequiredParts(String requiredParts) {
        if (requiredParts == null || requiredParts.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(requiredParts.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
