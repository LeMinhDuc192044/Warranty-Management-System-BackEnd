package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCreateDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyListDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyResponseDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyUpdateDTO;
import com.warrantyclaim.warrantyclaim_api.entity.*;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.mapper.WarrantyPolicyMapper;
import com.warrantyclaim.warrantyclaim_api.repository.ElectricVehicleTypeRepository;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsTypeEVMRepository;
import com.warrantyclaim.warrantyclaim_api.repository.ProductsSparePartsTypeSCRepository;
import com.warrantyclaim.warrantyclaim_api.repository.WarrantyPolicyRepository;
import com.warrantyclaim.warrantyclaim_api.service.WarrantyPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarrantyPolicyServiceImpl implements WarrantyPolicyService {
    private final WarrantyPolicyMapper mapper;
    private final WarrantyPolicyRepository warrantyPolicyRepository;
    private final ElectricVehicleTypeRepository electricVehicleTypeRepository;
    private final ProductsSparePartsTypeSCRepository sparePartsTypeSCRepository;
    private final ProductsSparePartsTypeEVMRepository sparePartsTypeEVMRepository;
    private final com.warrantyclaim.warrantyclaim_api.repository.ElectricVehicleRepository electricVehicleRepository;

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO createWarrantyPolicy(WarrantyPolicyCreateDTO createDTO) {
        // Convert DTO to entity
        WarrantyPolicy policy = mapper.toEntity(createDTO);
        policy.setId(generateId());
        warrantyPolicyRepository.save(policy);
        // Associate vehicle types
        if (createDTO.getVehicleTypeIds() != null && !createDTO.getVehicleTypeIds().isEmpty()) {
            for (String vehicleTypeId : createDTO.getVehicleTypeIds()) {
                ElectricVehicleType vehicleType = electricVehicleTypeRepository.findById(vehicleTypeId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Vehicle type not found with ID: " + vehicleTypeId));
                policy.addVehicleType(vehicleType);
            }
        }

        // Associate SC spare parts types
        if (createDTO.getSparePartsTypeSCIds() != null && !createDTO.getSparePartsTypeSCIds().isEmpty()) {
            for (String sparePartsTypeId : createDTO.getSparePartsTypeSCIds()) {
                ProductsSparePartsTypeSC sparePartsType = sparePartsTypeSCRepository.findById(sparePartsTypeId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "SC spare parts type not found with ID: " + sparePartsTypeId));
                policy.addSparePartsTypeSC(sparePartsType);
            }
        }

        // Associate EVM spare parts types
        if (createDTO.getSparePartsTypeEVMIds() != null && !createDTO.getSparePartsTypeEVMIds().isEmpty()) {
            for (String sparePartsTypeId : createDTO.getSparePartsTypeEVMIds()) {
                ProductsSparePartsTypeEVM sparePartsType = sparePartsTypeEVMRepository.findById(sparePartsTypeId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "EVM spare parts type not found with ID: " + sparePartsTypeId));
                policy.addSparePartsTypeEVM(sparePartsType);
            }
        }

        // Save and return
        policy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(policy);
    }

    @Override
    @Transactional(readOnly = true)
    public WarrantyPolicyResponseDTO getWarrantyPolicyById(String id) {
        WarrantyPolicy policy = warrantyPolicyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + id));
        return mapper.toResponseDTO(policy);
    }

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO updateWarrantyPolicy(String id, WarrantyPolicyUpdateDTO updateDTO) {
        // Find policy
        WarrantyPolicy policy = warrantyPolicyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + id));

        // Update basic fields
        mapper.updateEntity(policy, updateDTO);
        // Update vehicle types if provided
        if (updateDTO.getVehicleTypeIds() != null) {
            List<ElectricVehicleType> vehicleTypes = new ArrayList<>();
            for (String vehicleTypeId : updateDTO.getVehicleTypeIds()) {
                ElectricVehicleType vehicleType = electricVehicleTypeRepository.findById(vehicleTypeId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Vehicle type not found with ID: " + vehicleTypeId));
                vehicleTypes.add(vehicleType);
            }
            policy.setElectricVehicleTypes(vehicleTypes);
        }

        // Update SC spare parts types if provided
        if (updateDTO.getSparePartsTypeSCIds() != null) {
            List<ProductsSparePartsTypeSC> sparePartsTypes = new ArrayList<>();
            for (String sparePartsTypeId : updateDTO.getSparePartsTypeSCIds()) {
                ProductsSparePartsTypeSC sparePartsType = sparePartsTypeSCRepository.findById(sparePartsTypeId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "SC spare parts type not found with ID: " + sparePartsTypeId));
                sparePartsTypes.add(sparePartsType);
            }
            policy.setSparePartsTypesSC(sparePartsTypes);
        }

        // Update EVM spare parts types if provided
        if (updateDTO.getSparePartsTypeEVMIds() != null) {
            List<ProductsSparePartsTypeEVM> sparePartsTypes = new ArrayList<>();
            for (String sparePartsTypeId : updateDTO.getSparePartsTypeEVMIds()) {
                ProductsSparePartsTypeEVM sparePartsType = sparePartsTypeEVMRepository.findById(sparePartsTypeId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "EVM spare parts type not found with ID: " + sparePartsTypeId));
                sparePartsTypes.add(sparePartsType);
            }
            policy.setSparePartsTypesEVM(sparePartsTypes);
        }

        // Save and return
        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(updatedPolicy);
    }

    @Override
    @Transactional
    public void deleteWarrantyPolicy(String id) {
        if (!warrantyPolicyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warranty policy not found with ID: " + id);
        }
        warrantyPolicyRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarrantyPolicyListDTO> getAllWarrantyPolicies(Pageable pageable) {
        Page<WarrantyPolicy> policies = warrantyPolicyRepository.findAll(pageable);
        return policies.map(mapper::toListDTO);
    }

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO assignVehicleTypes(String policyId, List<String> vehicleTypeIds) {
        WarrantyPolicy policy = warrantyPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + policyId));

        // Find and add vehicle types
        List<ElectricVehicleType> vehicleTypes = new ArrayList<>();
        for (String vehicleTypeId : vehicleTypeIds) {
            ElectricVehicleType vehicleType = electricVehicleTypeRepository.findById(vehicleTypeId)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Vehicle type not found with ID: " + vehicleTypeId));
            vehicleTypes.add(vehicleType);
        }

        // Set vehicle types (replace existing)
        policy.setElectricVehicleTypes(vehicleTypes);

        // Save and return
        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(updatedPolicy);
    }

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO addVehicleType(String policyId, String vehicleTypeId) {
        WarrantyPolicy policy = warrantyPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + policyId));

        ElectricVehicleType vehicleType = electricVehicleTypeRepository.findById(vehicleTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found with ID: " + vehicleTypeId));

        // Check if already assigned
        boolean exists = policy.getWarrantyPolicyElectricVehicleTypes().stream()
                .anyMatch(wp -> wp.getVehicleType().getId().equals(vehicleTypeId));

        // Add only if not already linked
        if (!exists) {
            policy.addVehicleType(vehicleType);
        }

        // Save and return
        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(updatedPolicy);
    }

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO removeVehicleType(String policyId, String vehicleTypeId) {
        WarrantyPolicy policy = warrantyPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + policyId));

        policy.removeVehicleType(vehicleTypeId);

        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(updatedPolicy);
    }

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO assignSparePartsTypeSC(String policyId, List<String> sparePartsTypeSCIds) {
        WarrantyPolicy policy = warrantyPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + policyId));

        // Find and add spare parts types
        List<ProductsSparePartsTypeSC> sparePartsTypes = new ArrayList<>();
        for (String sparePartsTypeId : sparePartsTypeSCIds) {
            ProductsSparePartsTypeSC sparePartsType = sparePartsTypeSCRepository.findById(sparePartsTypeId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "SC spare parts type not found with ID: " + sparePartsTypeId));
            sparePartsTypes.add(sparePartsType);
        }

        // Set spare parts types (replace existing)
        policy.setSparePartsTypesSC(sparePartsTypes);

        // Save and return
        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(updatedPolicy);
    }

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO addSparePartsTypeSC(String policyId, String sparePartsTypeId) {
        WarrantyPolicy policy = warrantyPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + policyId));

        ProductsSparePartsTypeSC sparePartsType = sparePartsTypeSCRepository.findById(sparePartsTypeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SC spare parts type not found with ID: " + sparePartsTypeId));

        // Check if already assigned
        boolean exists = policy.getWarrantyPolicyProductsSparePartsTypeSCs().stream()
                .anyMatch(wp -> wp.getPartType().getId().equals(sparePartsTypeId));

        // Add only if not already linked
        if (!exists) {
            policy.addSparePartsTypeSC(sparePartsType);
        }

        // Save and return
        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(updatedPolicy);
    }

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO removeSparePartsTypeSC(String policyId, String sparePartsTypeId) {
        WarrantyPolicy policy = warrantyPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + policyId));

        policy.removeSparePartsTypeSC(sparePartsTypeId);

        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(updatedPolicy);
    }

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO assignSparePartsTypeEVM(String policyId, List<String> sparePartsTypeEVMIds) {
        WarrantyPolicy policy = warrantyPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + policyId));

        // Find and add spare parts types
        List<ProductsSparePartsTypeEVM> sparePartsTypes = new ArrayList<>();
        for (String sparePartsTypeId : sparePartsTypeEVMIds) {
            ProductsSparePartsTypeEVM sparePartsType = sparePartsTypeEVMRepository.findById(sparePartsTypeId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "EVM spare parts type not found with ID: " + sparePartsTypeId));
            sparePartsTypes.add(sparePartsType);
        }

        // Set spare parts types (replace existing)
        policy.setSparePartsTypesEVM(sparePartsTypes);

        // Save and return
        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(updatedPolicy);
    }

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO addSparePartsTypeEVM(String policyId, String sparePartsTypeId) {
        WarrantyPolicy policy = warrantyPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + policyId));

        ProductsSparePartsTypeEVM sparePartsType = sparePartsTypeEVMRepository.findById(sparePartsTypeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "EVM spare parts type not found with ID: " + sparePartsTypeId));

        // Check if already assigned
        boolean exists = policy.getWarrantyPoliciesEvmTypes().stream()
                .anyMatch(wp -> wp.getPartType().getId().equals(sparePartsTypeId));

        // Add only if not already linked
        if (!exists) {
            policy.addSparePartsTypeEVM(sparePartsType);
        }

        // Save and return
        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(updatedPolicy);
    }

    @Override
    @Transactional
    public WarrantyPolicyResponseDTO removeSparePartsTypeEVM(String policyId, String sparePartsTypeId) {
        WarrantyPolicy policy = warrantyPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty policy not found with ID: " + policyId));

        policy.removeSparePartsTypeEVM(sparePartsTypeId);

        WarrantyPolicy updatedPolicy = warrantyPolicyRepository.save(policy);
        return mapper.toResponseDTO(updatedPolicy);
    }

    private String generateId() {
        return "WP-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    @Transactional(readOnly = true)
    public com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO checkWarrantyEligibility(
            String vehicleVIN) {
        com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO.WarrantyPolicyCheckResponseDTOBuilder responseBuilder = com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO
                .builder();

        List<String> reasons = new ArrayList<>();
        List<com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyDetailDTO> applicablePolicies = new ArrayList<>();

        // Track failure reasons to avoid duplicates
        boolean hasExpiredPolicies = false;
        boolean hasUsageTypeMismatch = false;
        boolean hasMileageExceeded = false;
        LocalDate earliestExpiry = null;

        // 1. Find vehicle by VIN
        ElectricVehicle vehicle = electricVehicleRepository.findById(vehicleVIN)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with VIN: " + vehicleVIN));

        // 2. Get vehicle information
        ElectricVehicleType vehicleType = vehicle.getVehicleType();
        if (vehicleType == null) {
            reasons.add("Xe chưa có thông tin loại xe");
            return responseBuilder
                    .isEligible(false)
                    .message("Không thể kiểm tra bảo hành: Xe chưa có thông tin loại xe")
                    .reasons(reasons)
                    .build();
        }

        LocalDate purchaseDate = vehicle.getPurchaseDate();
        Float totalKm = vehicle.getTotalKm();
        com.warrantyclaim.warrantyclaim_api.enums.UsageType usageType = vehicle.getUsageType();

        // 3. Get current date for warranty expiry check
        LocalDate today = LocalDate.now();

        // 4. Get all warranty policies for this vehicle type
        List<WarrantyPolicy> allPolicies = warrantyPolicyRepository.findAll();

        // 5. Check each policy against vehicle information
        for (WarrantyPolicy policy : allPolicies) {
            // Check if policy applies to this vehicle type
            boolean appliesToVehicleType = policy.getWarrantyPolicyElectricVehicleTypes().stream()
                    .anyMatch(wp -> wp.getVehicleTypeId().equals(vehicleType.getId()));

            if (!appliesToVehicleType) {
                continue; // Skip this policy
            }

            // Check warranty expiry date
            boolean isExpired = false;
            String expiryReason = null;
            if (purchaseDate != null && policy.getCoverageDurationMonths() != null) {
                LocalDate warrantyExpiryDate = purchaseDate.plusMonths(policy.getCoverageDurationMonths());
                if (today.isAfter(warrantyExpiryDate)) {
                    isExpired = true;
                    expiryReason = "Hết hạn bảo hành (Đến " + warrantyExpiryDate + ")";
                }
            }

<<<<<<< HEAD
            // Usage type check - Filter policies based on vehicle usage type
            // Applies to ALL vehicle types: VF3, VF5, VF6, VF7, VF8, VF9, Limo Green, etc.
            boolean usageTypeCompatible = true;
            String usageTypeReason = null;

            if (usageType != null && policy.getName() != null) {
                String policyNameLower = policy.getName().toLowerCase();

                // DEBUG: Log policy name and vehicle usage type
                System.out.println("=== USAGE TYPE CHECK ===");
                System.out.println("Vehicle UsageType: " + usageType);
                System.out.println("Policy Name: " + policy.getName());
                System.out.println("Policy Name (lowercase): " + policyNameLower);

                // Check if policy is for PERSONAL use
                boolean isPolicyForPersonal = policyNameLower.contains("cá nhân")
                        || policyNameLower.contains("personal");

                // Check if policy is for COMMERCIAL use
                boolean isPolicyForCommercial = policyNameLower.contains("thương mại")
                        || policyNameLower.contains("commercial");

                System.out.println("Is For Personal: " + isPolicyForPersonal);
                System.out.println("Is For Commercial: " + isPolicyForCommercial);

                // If policy has usage type specified, check compatibility
                if (isPolicyForPersonal || isPolicyForCommercial) {
                    if (usageType == com.warrantyclaim.warrantyclaim_api.enums.UsageType.PERSONAL
                            && isPolicyForCommercial) {
                        usageTypeCompatible = false;
                        usageTypeReason = "Chính sách dành cho xe thương mại, không áp dụng cho xe cá nhân";
                    } else if (usageType == com.warrantyclaim.warrantyclaim_api.enums.UsageType.COMMERCIAL
                            && isPolicyForPersonal) {
                        usageTypeCompatible = false;
                        usageTypeReason = "Chính sách dành cho xe cá nhân, không áp dụng cho xe thương mại";
                    }
                }
                // If policy doesn't specify usage type (general policies), it applies to all

                System.out.println("Usage Type Compatible: " + usageTypeCompatible);
                System.out.println("========================");
            }

=======
            // Usage type check removed - both personal and commercial vehicles have
            // warranty policies
            // No need to filter by usage type, just show applicable policies based on
            // expiry and mileage
            boolean usageTypeCompatible = true;
            String usageTypeReason = null;

>>>>>>> origin/main
            // Check mileage limit (if applicable) - Enhanced to check multiple limits
            boolean mileageExceeded = false;
            String mileageReason = null;
            if (totalKm != null && policy.getDescription() != null) {
                String desc = policy.getDescription();

                // Check for different mileage limits in description
                if ((desc.contains("200,000 km") || desc.contains("200.000 km")) && totalKm > 200000) {
                    mileageExceeded = true;
<<<<<<< HEAD
                    mileageReason = "Vượt quá giới hạn 200,000 km (Hiện tại: " + String.format("%.0f", totalKm)
                            + " km)";
                } else if ((desc.contains("160,000 km") || desc.contains("160.000 km")) && totalKm > 160000) {
                    mileageExceeded = true;
                    mileageReason = "Vượt quá giới hạn 160,000 km (Hiện tại: " + String.format("%.0f", totalKm)
                            + " km)";
                } else if ((desc.contains("130,000 km") || desc.contains("130.000 km")) && totalKm > 130000) {
                    mileageExceeded = true;
                    mileageReason = "Vượt quá giới hạn 130,000 km (Hiện tại: " + String.format("%.0f", totalKm)
                            + " km)";
                } else if ((desc.contains("100,000 km") || desc.contains("100.000 km")) && totalKm > 100000) {
                    mileageExceeded = true;
                    mileageReason = "Vượt quá giới hạn 100,000 km (Hiện tại: " + String.format("%.0f", totalKm)
                            + " km)";
=======
                    mileageReason = "Vượt quá giới hạn 200,000 km (Hiện tại: " + String.format("%.0f", totalKm) + " km)";
                } else if ((desc.contains("160,000 km") || desc.contains("160.000 km")) && totalKm > 160000) {
                    mileageExceeded = true;
                    mileageReason = "Vượt quá giới hạn 160,000 km (Hiện tại: " + String.format("%.0f", totalKm) + " km)";
                } else if ((desc.contains("130,000 km") || desc.contains("130.000 km")) && totalKm > 130000) {
                    mileageExceeded = true;
                    mileageReason = "Vượt quá giới hạn 130,000 km (Hiện tại: " + String.format("%.0f", totalKm) + " km)";
                } else if ((desc.contains("100,000 km") || desc.contains("100.000 km")) && totalKm > 100000) {
                    mileageExceeded = true;
                    mileageReason = "Vượt quá giới hạn 100,000 km (Hiện tại: " + String.format("%.0f", totalKm) + " km)";
>>>>>>> origin/main
                } else if ((desc.contains("80,000 km") || desc.contains("80.000 km")) && totalKm > 80000) {
                    mileageExceeded = true;
                    mileageReason = "Vượt quá giới hạn 80,000 km (Hiện tại: " + String.format("%.0f", totalKm) + " km)";
                } else if ((desc.contains("40,000 km") || desc.contains("40.000 km")) && totalKm > 40000) {
                    mileageExceeded = true;
                    mileageReason = "Vượt quá giới hạn 40,000 km (Hiện tại: " + String.format("%.0f", totalKm) + " km)";
                }
            }

            // Determine if policy is applicable
            boolean isApplicable = !isExpired && usageTypeCompatible && !mileageExceeded;

            // Build policy detail with enhanced information
            List<String> policyReasons = new ArrayList<>();
            if (isExpired)
                policyReasons.add(expiryReason);
            if (!usageTypeCompatible)
                policyReasons.add(usageTypeReason);
            if (mileageExceeded)
                policyReasons.add(mileageReason);

            // Add success reason if applicable
            if (isApplicable) {
                if (purchaseDate != null && policy.getCoverageDurationMonths() != null) {
                    LocalDate warrantyExpiryDate = purchaseDate.plusMonths(policy.getCoverageDurationMonths());
                    policyReasons.add("✅ Còn hiệu lực đến " + warrantyExpiryDate);
                }
            }

            com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyDetailDTO policyDetail = com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyDetailDTO
                    .builder()
                    .policyId(policy.getId())
                    .policyName(policy.getName())
                    .description(policy.getDescription())
                    .coverageType(policy.getCoverageTypeWarrantyPolicy() != null
                            ? policy.getCoverageTypeWarrantyPolicy().name()
                            : "UNKNOWN")
                    .coverageDurationMonths(policy.getCoverageDurationMonths())
<<<<<<< HEAD
                    .coverageMileage(policy.getCoverageMileage())
=======
>>>>>>> origin/main
                    .isApplicable(isApplicable)
                    .reasons(policyReasons)
                    .build();

<<<<<<< HEAD
            // Only add policies that pass usage type check
            // Usage type incompatible policies should be completely filtered out, not shown
            // as "not applicable"
            if (usageTypeCompatible) {
                applicablePolicies.add(policyDetail);
            }
=======
            // Add ALL policies to response (both applicable and not applicable)
            applicablePolicies.add(policyDetail);
>>>>>>> origin/main

            // Track failure types for summary reasons
            if (!isApplicable) {
                if (isExpired) {
                    hasExpiredPolicies = true;
                    LocalDate warrantyExpiryDate = purchaseDate.plusMonths(policy.getCoverageDurationMonths());
                    if (earliestExpiry == null || warrantyExpiryDate.isBefore(earliestExpiry)) {
                        earliestExpiry = warrantyExpiryDate;
                    }
                }
                if (!usageTypeCompatible) {
                    hasUsageTypeMismatch = true;
                }
                if (mileageExceeded) {
                    hasMileageExceeded = true;
                }
            }
        }

        // 6. Build final response with summarized reasons
        // Count only applicable policies
        long applicableCount = applicablePolicies.stream()
                .filter(p -> p.isApplicable())
                .count();

        boolean isEligible = applicableCount > 0;
        String message;
        String vehicleTypeName = vehicleType.getModelName() != null ? vehicleType.getModelName() : vehicleType.getId();

        if (isEligible) {
            message = "Xe " + vehicleTypeName + " (VIN: " + vehicleVIN + ") đủ điều kiện bảo hành với " +
                    applicableCount + " chính sách áp dụng được.";
        } else {
            message = "Xe " + vehicleTypeName + " (VIN: " + vehicleVIN + ") KHÔNG đủ điều kiện bảo hành.";

            // Summarize reasons intelligently (no duplicates)
            if (hasExpiredPolicies && earliestExpiry != null) {
                reasons.add("⏰ Bảo hành đã hết hạn (sớm nhất: " + earliestExpiry + ")");
            }
<<<<<<< HEAD
            if (hasUsageTypeMismatch && usageType != null) {
                String usageTypeVi = (usageType == com.warrantyclaim.warrantyclaim_api.enums.UsageType.COMMERCIAL)
                        ? "thương mại"
                        : "cá nhân";
                reasons.add("🚗 Một số chính sách không áp dụng cho xe " + usageTypeVi);
            }
=======
            // Removed usage type mismatch - both personal and commercial vehicles have
            // warranties
>>>>>>> origin/main
            if (hasMileageExceeded && totalKm != null) {
                reasons.add("📊 Số km đã chạy (" + String.format("%.0f", totalKm) + " km) vượt quá giới hạn bảo hành");
            }

            if (reasons.isEmpty()) {
                reasons.add("Không tìm thấy chính sách bảo hành phù hợp cho loại xe này");
            }
        }

        // Add vehicle summary to reasons
        List<String> vehicleInfo = new ArrayList<>();
        vehicleInfo.add("📋 Thông tin xe:");
        vehicleInfo.add("  • Loại xe: " + vehicleTypeName);
        if (purchaseDate != null) {
            vehicleInfo.add("  • Ngày mua: " + purchaseDate);
        }
        if (totalKm != null) {
            vehicleInfo.add("  • Số km: " + String.format("%.0f", totalKm) + " km");
        }
        if (usageType != null) {
            vehicleInfo.add("  • Mục đích: "
                    + (usageType == com.warrantyclaim.warrantyclaim_api.enums.UsageType.COMMERCIAL ? "Thương mại"
<<<<<<< HEAD
                            : "Cá nhân"));
=======
                    : "Cá nhân"));
>>>>>>> origin/main
        }

        return responseBuilder
                .isEligible(isEligible)
                .message(message)
                .applicablePolicies(applicablePolicies)
                .vehicleType(vehicleTypeName)
                .purchaseDate(purchaseDate)
                .vehicleAgeMonths(purchaseDate != null
                        ? (int) java.time.temporal.ChronoUnit.MONTHS.between(purchaseDate, LocalDate.now())
                        : null)
                .reasons(reasons)
                .additionalInfo(vehicleInfo)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO checkWarrantyByVehicleType(
            String vehicleTypeId) {
        com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO.WarrantyPolicyCheckResponseDTOBuilder responseBuilder = com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCheckResponseDTO
                .builder();

        List<String> reasons = new ArrayList<>();
        List<com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyDetailDTO> applicablePolicies = new ArrayList<>();

        // Find vehicle type
        ElectricVehicleType vehicleType = electricVehicleTypeRepository.findById(vehicleTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found with ID: " + vehicleTypeId));

        // Get all warranty policies
        List<WarrantyPolicy> allPolicies = warrantyPolicyRepository.findAll();

        // Check which policies apply to this vehicle type
        for (WarrantyPolicy policy : allPolicies) {
            boolean isApplicable = policy.getWarrantyPolicyElectricVehicleTypes().stream()
                    .anyMatch(wp -> wp.getVehicleTypeId().equals(vehicleTypeId));

            if (isApplicable) {
                com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyDetailDTO policyDetail = com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyDetailDTO
                        .builder()
                        .policyId(policy.getId())
                        .policyName(policy.getName())
                        .description(policy.getDescription())
                        .coverageType(policy.getCoverageTypeWarrantyPolicy() != null
                                ? policy.getCoverageTypeWarrantyPolicy().name()
                                : "UNKNOWN")
                        .coverageDurationMonths(policy.getCoverageDurationMonths())
<<<<<<< HEAD
                        .coverageMileage(policy.getCoverageMileage())
=======
>>>>>>> origin/main
                        .isApplicable(true)
                        .build();
                applicablePolicies.add(policyDetail);
            }
        }

        // Build response
        boolean isEligible = !applicablePolicies.isEmpty();
        String message;
        String vehicleTypeName = vehicleType.getModelName() != null ? vehicleType.getModelName() : vehicleTypeId;

        if (isEligible) {
            message = "Xe loại \"" + vehicleTypeName + "\" đủ điều kiện bảo hành với " +
                    applicablePolicies.size() + " chính sách áp dụng được.";
        } else {
            message = "Xe loại \"" + vehicleTypeName + "\" không có chính sách bảo hành áp dụng.";
            reasons.add("Không tìm thấy chính sách bảo hành phù hợp cho loại xe này");
        }

        return responseBuilder
                .isEligible(isEligible)
                .message(message)
                .applicablePolicies(applicablePolicies)
                .vehicleType(vehicleTypeName)
                .reasons(reasons)
                .build();
    }

}