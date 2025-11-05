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
@Table(name = "SC_Admin")
public class SCAdmin {

    @Id
    @Column(name = "SC_AdminID", length = 50)
    private String id;

    @Column(name = "UserID")
    private Long userId;

    @Column(name = "Account_Name", length = 100)
    private String accountName;

    private Integer age;

    @Column(length = 100)
    private String email;

    @Column(name = "Phone_Number", length = 20)
    private String phoneNumber;

    @Column(name = "Date_of_Birth")
    private LocalDate dateOfBirth;

    @Column(name = "BranchOffice", length = 150)
    private String branchOffice;

    @Column(name = "District", length = 100)
    private String district;

    @Column(length = 100)
    private String password;
}
