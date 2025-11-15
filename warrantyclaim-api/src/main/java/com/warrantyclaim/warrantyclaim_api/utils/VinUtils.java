package com.warrantyclaim.warrantyclaim_api.utils;

import java.util.List;

public final class VinUtils {

    private VinUtils() {
        // Ngăn không cho khởi tạo class tiện ích
    }

    /**
     * Kiểm tra VIN theo chuẩn quốc tế ISO 3779 (17 ký tự)
     * - 3 ký tự đầu (WMI): VNA, VNB (World Manufacturer Identifier - VinFast
     * Vietnam)
     * - 6 ký tự tiếp (VDS): Mô tả xe (model, động cơ, variant)
     * - 8 ký tự cuối (VIS): Năm sản xuất + serial number
     */
    public static boolean isValidVin(String vin) {
        if (vin == null || vin.length() != 17)
            return false;
        // WMI (3): VNA/VNB + VDS (6): alphanumeric + VIS (8): alphanumeric
        String pattern = "^VN[A-Z][0-9A-Z]{6}[0-9A-Z]{8}$";
        return vin.matches(pattern);
    }

    /**
     * Tạo VIN theo chuẩn ISO 3779
<<<<<<< HEAD
     * 
=======
     *
>>>>>>> origin/main
     * @param wmiCode      3 ký tự WMI (VNA, VNB...)
     * @param modelCode    Mã model xe (VF3, VF5, VF6, VF7, VF8, VF9, E34, LMG, MNG,
     *                     HRG, NRG)
     * @param variant      Biến thể (P=Plus, S=Standard, E=Eco, L=Lux)
     * @param motorType    Loại động cơ/pin (E=Electric)
     * @param year         Năm sản xuất (ký tự: A=2010, B=2011... P=2023, R=2024,
     *                     S=2025...)
     * @param serialNumber Số thứ tự sản xuất (6-7 chữ số)
     */
    public static String generateVin(String wmiCode, String modelCode, char variant,
            char motorType, char year, int serialNumber) {
        String wmi = wmiCode.trim().toUpperCase();
        String model = modelCode.trim().toUpperCase();
        char variantChar = Character.toUpperCase(variant);
        char motorChar = Character.toUpperCase(motorType);
        char yearChar = Character.toUpperCase(year);

        List<String> validWMI = List.of("VNA", "VNB", "VNC");
        List<String> validModels = List.of("VF3", "VF5", "VF6", "VF7", "VF8", "VF9", "E34", "LMG", "MNG", "HRG", "NRG");

        if (!validWMI.contains(wmi)) {
            throw new IllegalArgumentException("WMI không hợp lệ (VNA/VNB/VNC): " + wmi);
        }
        if (!validModels.contains(model)) {
            throw new IllegalArgumentException("Model không hợp lệ: " + model);
        }

        // VDS (6 chars): Model(3) + Variant(1) + Motor(1) + Check(1)
        String vds = String.format("%-3s%c%c0", model, variantChar, motorChar).replace(' ', '0');

        // VIS (8 chars): Year(1) + Plant(1) + Serial(6)
        String vis = String.format("%cH%06d", yearChar, serialNumber);

        return wmi + vds + vis;
    }
}
