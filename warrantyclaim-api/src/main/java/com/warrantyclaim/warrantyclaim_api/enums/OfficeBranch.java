package com.warrantyclaim.warrantyclaim_api.enums;

public enum OfficeBranch {

    D1("VinFast HCMC Head Office", "29 Nguyen Thi Minh Khai, District 1, HCMC"),
    DISTRICT3("VinFast Showroom District 3", "194 Nam Ky Khoi Nghia, District 3, HCMC"),
    DISTRICT5("VinFast Service Center District 5", "55 An Duong Vuong, District 5, HCMC"),
    DISTRICT7("VinFast Service Center District 7", "23 Nguyen Van Linh, District 7, HCMC"),
    THU_DUC("VinFast Showroom Thu Duc City", "1 Vo Van Ngan, Thu Duc City, HCMC"),
    TAN_BINH("VinFast Service Center Tan Binh", "20 Truong Chinh, Tan Binh District, HCMC"),
    BINH_THANH("VinFast Experience Center Binh Thanh", "215 Dien Bien Phu, Binh Thanh District, HCMC"),
    PHU_NHUAN("VinFast Showroom Phu Nhuan", "50 Nguyen Kiem, Phu Nhuan District, HCMC"),
    GO_VAP("VinFast Service Center Go Vap", "120 Quang Trung, Go Vap District, HCMC"),
    TAN_PHU("VinFast Parts Warehouse Tan Phu", "200 Au Co, Tan Phu District, HCMC");

    private final String branchName;
    private final String address;

    OfficeBranch(String branchName, String address) {
        this.branchName = branchName;
        this.address = address;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getAddress() {
        return address;
    }
}
