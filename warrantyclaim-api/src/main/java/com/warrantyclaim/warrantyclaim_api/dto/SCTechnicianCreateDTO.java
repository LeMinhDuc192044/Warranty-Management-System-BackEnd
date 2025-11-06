package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SCTechnicianCreateDTO {
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String password;
    private String branchOffice;
    private String specialty;
}
