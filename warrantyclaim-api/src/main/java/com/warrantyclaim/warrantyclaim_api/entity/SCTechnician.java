package com.warrantyclaim.warrantyclaim_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "SC_Technician")
public class SCTechnician {
    @Id
    @Column(name = "SC_TechnicianID", length = 50)
    private String id;

    @Column(name = "UserID")
    private Long userId;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(name = "Phone_Number", length = 20)
    private String phoneNumber;

    @Column(name = "Date_of_Birth")
    private LocalDate dateOfBirth;

    @Column(length = 100)
    private String password;

    @Column(name = "BranchOffice", length = 150)
    private String branchOffice;

    @Column(name = "District", length = 100)
    private String district;

    @Column(length = 100)
    private String specialty;

}
