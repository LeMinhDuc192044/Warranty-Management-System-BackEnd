package com.warrantyclaim.warrantyclaim_api.config;

import com.warrantyclaim.warrantyclaim_api.entity.*;
import com.warrantyclaim.warrantyclaim_api.enums.CoverageTypeWarrantyPolicy;
import com.warrantyclaim.warrantyclaim_api.enums.Role;
import com.warrantyclaim.warrantyclaim_api.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepo) {
        return args -> {
            String email = "evmadmin@example.com";
            if (!userRepo.existsByEmail(email)) {
                User admin = new User();
                admin.setUsername("EVM Admin");
                admin.setEmail(email);
                admin.setPassword(BCrypt.hashpw("evmadmin123", BCrypt.gensalt()));
                admin.setRoles(Set.of(Role.EVM_ADMIN));
                userRepo.save(admin);
                System.out.println("✅ EVM_ADMIN account created");
            } else {
                System.out.println("✅ EVM_ADMIN account already exists");
            }
        };
    }

    @Bean
    public CommandLineRunner initVehicleTypesAndWarrantyPolicies(
            ElectricVehicleTypeRepository vehicleTypeRepo,
            WarrantyPolicyRepository policyRepo) {
        return args -> {

            // ==================== STEP 1: Create VinFast Vehicle Types
            // ====================
            if (vehicleTypeRepo.count() == 0) {
                System.out.println("\n🚗 Creating VinFast vehicle types...");

                // VF3 - MiniCar (Phổ thông)
                ElectricVehicleType vf3 = new ElectricVehicleType();
                vf3.setId("VF3");
                vf3.setModelName("VinFast VF3");
                vf3.setDescription("MiniCar điện thành thị - Nhỏ gọn, tiện lợi");
                vf3.setYearModelYear(2024);
                vf3.setBatteryType("LFP Battery 18.64 kWh");
                vf3.setPrice(235000000F); // 235 triệu
                vf3.setVehicleLine("VF3");
                vf3.setQuantity(0);

                // VF5 - A-SUV (Cơ bản)
                ElectricVehicleType vf5 = new ElectricVehicleType();
                vf5.setId("VF5");
                vf5.setModelName("VinFast VF5");
                vf5.setDescription("A-SUV điện thông minh - Phong cách trẻ trung");
                vf5.setYearModelYear(2024);
                vf5.setBatteryType("LFP Battery 37.2 kWh");
                vf5.setPrice(458000000F); // 458 triệu
                vf5.setVehicleLine("VF5");
                vf5.setQuantity(0);

                // VF6 - B-SUV (Trung cấp)
                ElectricVehicleType vf6 = new ElectricVehicleType();
                vf6.setId("VF6");
                vf6.setModelName("VinFast VF6");
                vf6.setDescription("B-SUV điện sang trọng - Tiện nghi và an toàn");
                vf6.setYearModelYear(2024);
                vf6.setBatteryType("LFP Battery 59.6 kWh");
                vf6.setPrice(675000000F); // 675 triệu
                vf6.setVehicleLine("VF6");
                vf6.setQuantity(0);

                // VF7 - C-SUV (Cao cấp)
                ElectricVehicleType vf7 = new ElectricVehicleType();
                vf7.setId("VF7");
                vf7.setModelName("VinFast VF7");
                vf7.setDescription("C-SUV điện đẳng cấp - Mạnh mẽ và tinh tế");
                vf7.setYearModelYear(2024);
                vf7.setBatteryType("LFP Battery 75.3 kWh");
                vf7.setPrice(850000000F); // 850 triệu
                vf7.setVehicleLine("VF7");
                vf7.setQuantity(0);

                // VF8 - D-SUV (Cao cấp hơn)
                ElectricVehicleType vf8 = new ElectricVehicleType();
                vf8.setId("VF8");
                vf8.setModelName("VinFast VF8");
                vf8.setDescription("D-SUV điện cao cấp - Công nghệ và sang trọng");
                vf8.setYearModelYear(2024);
                vf8.setBatteryType("NCM Battery 87.7 kWh");
                vf8.setPrice(1050000000F); // 1.05 tỷ
                vf8.setVehicleLine("VF8");
                vf8.setQuantity(0);

                // VF9 - E-SUV (Thượng lưu)
                ElectricVehicleType vf9 = new ElectricVehicleType();
                vf9.setId("VF9");
                vf9.setModelName("VinFast VF9");
                vf9.setDescription("E-SUV điện hạng sang - Đỉnh cao đẳng cấp");
                vf9.setYearModelYear(2024);
                vf9.setBatteryType("NCM Battery 123 kWh");
                vf9.setPrice(1491000000F); // 1.491 tỷ
                vf9.setVehicleLine("VF9");
                vf9.setQuantity(0);

                // VF e34 - Sedan (Phổ thông)
                ElectricVehicleType vfe34 = new ElectricVehicleType();
                vfe34.setId("VFE34");
                vfe34.setModelName("VinFast VF e34");
                vfe34.setDescription("Sedan điện thông minh - Tiết kiệm và hiện đại");
                vfe34.setYearModelYear(2023);
                vfe34.setBatteryType("LFP Battery 42 kWh");
                vfe34.setPrice(590000000F); // 590 triệu
                vfe34.setVehicleLine("e34");
                vfe34.setQuantity(0);

                vehicleTypeRepo.saveAll(List.of(vf3, vf5, vf6, vf7, vf8, vf9, vfe34));
                System.out.println("✅ Created 7 VinFast vehicle types (VF3, VF5, VF6, VF7, VF8, VF9, VF e34)");
            }

            // ==================== STEP 2: Create Warranty Policies ====================
            if (policyRepo.count() == 0) {
                System.out.println("\n📋 Creating VinFast warranty policies...");

                // Retrieve vehicle types
                ElectricVehicleType vf3 = vehicleTypeRepo.findById("VF3").orElse(null);
                ElectricVehicleType vf5 = vehicleTypeRepo.findById("VF5").orElse(null);
                ElectricVehicleType vf6 = vehicleTypeRepo.findById("VF6").orElse(null);
                ElectricVehicleType vf7 = vehicleTypeRepo.findById("VF7").orElse(null);
                ElectricVehicleType vf8 = vehicleTypeRepo.findById("VF8").orElse(null);
                ElectricVehicleType vf9 = vehicleTypeRepo.findById("VF9").orElse(null);
                ElectricVehicleType vfe34 = vehicleTypeRepo.findById("VFE34").orElse(null);

                // ===== POLICY 1: Bảo hành toàn diện xe 10 năm =====
                WarrantyPolicy policy1 = new WarrantyPolicy();
                policy1.setId("WP-VEHICLE-10Y");
                policy1.setName("Bảo hành toàn diện xe 10 năm");
                policy1.setDescription(
                        "Bảo hành toàn bộ xe (trừ pin) trong 10 năm hoặc 200,000 km, tùy điều kiện nào đến trước");
                policy1.setCoverageDurationMonths(120); // 10 năm
                policy1.setCoverageTypeWarrantyPolicy(CoverageTypeWarrantyPolicy.FULL);

                // Áp dụng cho TẤT CẢ xe VinFast
                if (vf3 != null)
                    policy1.addVehicleType(vf3);
                if (vf5 != null)
                    policy1.addVehicleType(vf5);
                if (vf6 != null)
                    policy1.addVehicleType(vf6);
                if (vf7 != null)
                    policy1.addVehicleType(vf7);
                if (vf8 != null)
                    policy1.addVehicleType(vf8);
                if (vf9 != null)
                    policy1.addVehicleType(vf9);
                if (vfe34 != null)
                    policy1.addVehicleType(vfe34);

                // ===== POLICY 2: Bảo hành pin 10 năm =====
                WarrantyPolicy policy2 = new WarrantyPolicy();
                policy2.setId("WP-BATTERY-10Y");
                policy2.setName("Bảo hành pin và hệ thống quản lý pin 10 năm");
                policy2.setDescription(
                        "Bảo hành pin, BMS và hệ thống sạc trong 10 năm hoặc 200,000 km. Đảm bảo dung lượng pin ≥ 70% sau thời gian bảo hành");
                policy2.setCoverageDurationMonths(120); // 10 năm
                policy2.setCoverageTypeWarrantyPolicy(CoverageTypeWarrantyPolicy.BATTERY);

                // Áp dụng cho TẤT CẢ xe điện VinFast
                if (vf3 != null)
                    policy2.addVehicleType(vf3);
                if (vf5 != null)
                    policy2.addVehicleType(vf5);
                if (vf6 != null)
                    policy2.addVehicleType(vf6);
                if (vf7 != null)
                    policy2.addVehicleType(vf7);
                if (vf8 != null)
                    policy2.addVehicleType(vf8);
                if (vf9 != null)
                    policy2.addVehicleType(vf9);
                if (vfe34 != null)
                    policy2.addVehicleType(vfe34);

                // ===== POLICY 3: Bảo hành động cơ điện 10 năm =====
                WarrantyPolicy policy3 = new WarrantyPolicy();
                policy3.setId("WP-MOTOR-10Y");
                policy3.setName("Bảo hành động cơ điện và bộ truyền động 10 năm");
                policy3.setDescription(
                        "Bảo hành động cơ điện, bộ biến tần, hệ thống truyền động trong 10 năm hoặc 200,000 km");
                policy3.setCoverageDurationMonths(120); // 10 năm
                policy3.setCoverageTypeWarrantyPolicy(CoverageTypeWarrantyPolicy.PARTS_ONLY);

                if (vf3 != null)
                    policy3.addVehicleType(vf3);
                if (vf5 != null)
                    policy3.addVehicleType(vf5);
                if (vf6 != null)
                    policy3.addVehicleType(vf6);
                if (vf7 != null)
                    policy3.addVehicleType(vf7);
                if (vf8 != null)
                    policy3.addVehicleType(vf8);
                if (vf9 != null)
                    policy3.addVehicleType(vf9);
                if (vfe34 != null)
                    policy3.addVehicleType(vfe34);

                // ===== POLICY 4: Bảo hành hệ thống phanh 5 năm =====
                WarrantyPolicy policy4 = new WarrantyPolicy();
                policy4.setId("WP-BRAKE-5Y");
                policy4.setName("Bảo hành hệ thống phanh và an toàn 5 năm");
                policy4.setDescription(
                        "Bảo hành hệ thống phanh ABS, túi khí, cảm biến an toàn trong 5 năm hoặc 100,000 km");
                policy4.setCoverageDurationMonths(60); // 5 năm
                policy4.setCoverageTypeWarrantyPolicy(CoverageTypeWarrantyPolicy.SUSPENSION);

                if (vf3 != null)
                    policy4.addVehicleType(vf3);
                if (vf5 != null)
                    policy4.addVehicleType(vf5);
                if (vf6 != null)
                    policy4.addVehicleType(vf6);
                if (vf7 != null)
                    policy4.addVehicleType(vf7);
                if (vf8 != null)
                    policy4.addVehicleType(vf8);
                if (vf9 != null)
                    policy4.addVehicleType(vf9);
                if (vfe34 != null)
                    policy4.addVehicleType(vfe34);

                // ===== POLICY 5: Bảo hành sơn và chống gỉ 5 năm =====
                WarrantyPolicy policy5 = new WarrantyPolicy();
                policy5.setId("WP-PAINT-5Y");
                policy5.setName("Bảo hành sơn ngoại thất và chống gỉ 5 năm");
                policy5.setDescription(
                        "Bảo hành sơn bong tróc, thân xe gỉ sét do lỗi nhà sản xuất trong 5 năm không giới hạn km");
                policy5.setCoverageDurationMonths(60); // 5 năm
                policy5.setCoverageTypeWarrantyPolicy(CoverageTypeWarrantyPolicy.PAINT);

                if (vf3 != null)
                    policy5.addVehicleType(vf3);
                if (vf5 != null)
                    policy5.addVehicleType(vf5);
                if (vf6 != null)
                    policy5.addVehicleType(vf6);
                if (vf7 != null)
                    policy5.addVehicleType(vf7);
                if (vf8 != null)
                    policy5.addVehicleType(vf8);
                if (vf9 != null)
                    policy5.addVehicleType(vf9);
                if (vfe34 != null)
                    policy5.addVehicleType(vfe34);

                // ===== POLICY 6: Bảo hành thân vỏ 3 năm =====
                WarrantyPolicy policy6 = new WarrantyPolicy();
                policy6.setId("WP-BODY-3Y");
                policy6.setName("Bảo hành thân vỏ và khung xe 3 năm");
                policy6.setDescription(
                        "Bảo hành thân vỏ, khung xe, cửa, nắp capo do lỗi nhà sản xuất trong 3 năm hoặc 100,000 km");
                policy6.setCoverageDurationMonths(36); // 3 năm
                policy6.setCoverageTypeWarrantyPolicy(CoverageTypeWarrantyPolicy.BODY);

                if (vf3 != null)
                    policy6.addVehicleType(vf3);
                if (vf5 != null)
                    policy6.addVehicleType(vf5);
                if (vf6 != null)
                    policy6.addVehicleType(vf6);
                if (vf7 != null)
                    policy6.addVehicleType(vf7);
                if (vf8 != null)
                    policy6.addVehicleType(vf8);
                if (vf9 != null)
                    policy6.addVehicleType(vf9);
                if (vfe34 != null)
                    policy6.addVehicleType(vfe34);

                // ===== POLICY 7: Bảo hành hệ thống điều hòa 3 năm =====
                WarrantyPolicy policy7 = new WarrantyPolicy();
                policy7.setId("WP-HVAC-3Y");
                policy7.setName("Bảo hành hệ thống điều hòa và sưởi 3 năm");
                policy7.setDescription("Bảo hành máy lạnh, quạt gió, hệ thống sưởi trong 3 năm hoặc 100,000 km");
                policy7.setCoverageDurationMonths(36); // 3 năm
                policy7.setCoverageTypeWarrantyPolicy(CoverageTypeWarrantyPolicy.PARTS_ONLY);

                if (vf3 != null)
                    policy7.addVehicleType(vf3);
                if (vf5 != null)
                    policy7.addVehicleType(vf5);
                if (vf6 != null)
                    policy7.addVehicleType(vf6);
                if (vf7 != null)
                    policy7.addVehicleType(vf7);
                if (vf8 != null)
                    policy7.addVehicleType(vf8);
                if (vf9 != null)
                    policy7.addVehicleType(vf9);
                if (vfe34 != null)
                    policy7.addVehicleType(vfe34);

                // ===== POLICY 8: Bảo hành phụ kiện chính hãng 1 năm =====
                WarrantyPolicy policy8 = new WarrantyPolicy();
                policy8.setId("WP-ACCESSORY-1Y");
                policy8.setName("Bảo hành phụ kiện chính hãng 1 năm");
                policy8.setDescription(
                        "Bảo hành phụ kiện chính hãng lắp thêm tại đại lý VinFast trong 1 năm kể từ ngày lắp đặt");
                policy8.setCoverageDurationMonths(12); // 1 năm
                policy8.setCoverageTypeWarrantyPolicy(CoverageTypeWarrantyPolicy.ACCESSORY);

                if (vf3 != null)
                    policy8.addVehicleType(vf3);
                if (vf5 != null)
                    policy8.addVehicleType(vf5);
                if (vf6 != null)
                    policy8.addVehicleType(vf6);
                if (vf7 != null)
                    policy8.addVehicleType(vf7);
                if (vf8 != null)
                    policy8.addVehicleType(vf8);
                if (vf9 != null)
                    policy8.addVehicleType(vf9);
                if (vfe34 != null)
                    policy8.addVehicleType(vfe34);

                // Save all policies
                policyRepo.saveAll(List.of(policy1, policy2, policy3, policy4, policy5, policy6, policy7, policy8));

                System.out.println("✅ Created 8 comprehensive warranty policies:");
                System.out.println("   1. WP-VEHICLE-10Y: Bảo hành toàn diện xe 10 năm/200,000 km");
                System.out.println("   2. WP-BATTERY-10Y: Bảo hành pin 10 năm/200,000 km (≥70%)");
                System.out.println("   3. WP-MOTOR-10Y: Bảo hành động cơ điện 10 năm/200,000 km");
                System.out.println("   4. WP-BRAKE-5Y: Bảo hành hệ thống phanh 5 năm/100,000 km");
                System.out.println("   5. WP-PAINT-5Y: Bảo hành sơn và chống gỉ 5 năm");
                System.out.println("   6. WP-BODY-3Y: Bảo hành thân vỏ 3 năm/100,000 km");
                System.out.println("   7. WP-HVAC-3Y: Bảo hành điều hòa 3 năm/100,000 km");
                System.out.println("   8. WP-ACCESSORY-1Y: Bảo hành phụ kiện 1 năm");
                System.out.println("   → Total: 56 policy-vehicle relationships created");
            } else {
                System.out.println("✅ Warranty policies already exist");
            }
        };
    }
}
