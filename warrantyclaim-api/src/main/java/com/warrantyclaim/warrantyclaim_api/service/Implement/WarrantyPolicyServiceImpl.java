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
    public class WarrantyPolicyServiceImpl implements WarrantyPolicyService{
        private final WarrantyPolicyMapper mapper;
        private final WarrantyPolicyRepository warrantyPolicyRepository;
        private final ElectricVehicleTypeRepository electricVehicleTypeRepository;
        private final ProductsSparePartsTypeSCRepository sparePartsTypeSCRepository;
        private final ProductsSparePartsTypeEVMRepository sparePartsTypeEVMRepository;
    
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
                                .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found with ID: " + vehicleTypeId));
                        policy.addVehicleType(vehicleType);
                    }
                }

                // Associate SC spare parts types
                if (createDTO.getSparePartsTypeSCIds() != null && !createDTO.getSparePartsTypeSCIds().isEmpty()) {
                    for (String sparePartsTypeId : createDTO.getSparePartsTypeSCIds()) {
                        ProductsSparePartsTypeSC sparePartsType = sparePartsTypeSCRepository.findById(sparePartsTypeId)
                                .orElseThrow(() -> new ResourceNotFoundException("SC spare parts type not found with ID: " + sparePartsTypeId));
                        policy.addSparePartsTypeSC(sparePartsType);
                    }
                }

                // Associate EVM spare parts types
                if (createDTO.getSparePartsTypeEVMIds() != null && !createDTO.getSparePartsTypeEVMIds().isEmpty()) {
                    for (String sparePartsTypeId : createDTO.getSparePartsTypeEVMIds()) {
                        ProductsSparePartsTypeEVM sparePartsType = sparePartsTypeEVMRepository.findById(sparePartsTypeId)
                                .orElseThrow(() -> new ResourceNotFoundException("EVM spare parts type not found with ID: " + sparePartsTypeId));
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
                            .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found with ID: " + vehicleTypeId));
                    vehicleTypes.add(vehicleType);
                }
                policy.setElectricVehicleTypes(vehicleTypes);
            }
    
            // Update SC spare parts types if provided
            if (updateDTO.getSparePartsTypeSCIds() != null) {
                List<ProductsSparePartsTypeSC> sparePartsTypes = new ArrayList<>();
                for (String sparePartsTypeId : updateDTO.getSparePartsTypeSCIds()) {
                    ProductsSparePartsTypeSC sparePartsType = sparePartsTypeSCRepository.findById(sparePartsTypeId)
                            .orElseThrow(() -> new ResourceNotFoundException("SC spare parts type not found with ID: " + sparePartsTypeId));
                    sparePartsTypes.add(sparePartsType);
                }
                policy.setSparePartsTypesSC(sparePartsTypes);
            }
    
            // Update EVM spare parts types if provided
            if (updateDTO.getSparePartsTypeEVMIds() != null) {
                List<ProductsSparePartsTypeEVM> sparePartsTypes = new ArrayList<>();
                for (String sparePartsTypeId : updateDTO.getSparePartsTypeEVMIds()) {
                    ProductsSparePartsTypeEVM sparePartsType = sparePartsTypeEVMRepository.findById(sparePartsTypeId)
                            .orElseThrow(() -> new ResourceNotFoundException("EVM spare parts type not found with ID: " + sparePartsTypeId));
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
                        .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found with ID: " + vehicleTypeId));
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
                        .orElseThrow(() -> new ResourceNotFoundException("SC spare parts type not found with ID: " + sparePartsTypeId));
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
                    .orElseThrow(() -> new ResourceNotFoundException("SC spare parts type not found with ID: " + sparePartsTypeId));
    
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
                        .orElseThrow(() -> new ResourceNotFoundException("EVM spare parts type not found with ID: " + sparePartsTypeId));
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
                    .orElseThrow(() -> new ResourceNotFoundException("EVM spare parts type not found with ID: " + sparePartsTypeId));
    
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
    
    
    }
