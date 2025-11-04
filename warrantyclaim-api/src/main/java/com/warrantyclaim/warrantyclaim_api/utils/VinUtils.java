package com.warrantyclaim.warrantyclaim_api.utils;

import java.util.List;

public final class VinUtils {

    private VinUtils() {
        // Ngăn không cho khởi tạo class tiện ích
    }

    public static boolean isValidVin(String vin) {
        if (vin == null || vin.length() != 17) return false;
        String pattern = "^VF(VF5|VF6|VF7|VF8|VF9|E34)\\d{4}[HT]{1}\\d{7}$";
        return vin.matches(pattern);
    }

    public static String generateVin(String modelCode, int year, char plantCode, int serialNumber) {
        String model = modelCode.trim().toUpperCase();
        char plant = Character.toUpperCase(plantCode);

        List<String> validModels = List.of("VF5", "VF6", "VF7", "VF8", "VF9", "E34");
        List<Character> validPlants = List.of('H', 'T');

        if (!validModels.contains(model)) {
            throw new IllegalArgumentException("Dòng xe không hợp lệ: " + model);
        }
        if (!validPlants.contains(plant)) {
            throw new IllegalArgumentException("Mã nhà máy không hợp lệ: " + plant);
        }

        return String.format("VF%s%d%c%07d", model, year, plant, serialNumber);
    }
}
