package com.warrantyclaim.warrantyclaim_api.enums;

public enum AccountStatus {
    ACTIVE, // Hoạt động - có thể login và sử dụng hệ thống
    LOCKED, // Tạm khóa - không thể login, có thể khôi phục
    INACTIVE // Ngừng hoạt động - vô hiệu hóa, có thể khôi phục
}
