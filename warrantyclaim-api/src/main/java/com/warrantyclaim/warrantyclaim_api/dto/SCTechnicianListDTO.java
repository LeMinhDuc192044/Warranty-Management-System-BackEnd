package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SCTechnicianListDTO {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String branchOffice;
    private String specialty;
}