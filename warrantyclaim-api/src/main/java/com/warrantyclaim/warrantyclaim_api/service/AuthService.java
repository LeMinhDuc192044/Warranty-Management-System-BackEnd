//package com.warrantyclaim.warrantyclaim_api.service;
//
//import com.warrantyclaim.warrantyclaim_api.dto.LoginRequest;
//import com.warrantyclaim.warrantyclaim_api.dto.LoginResponse;
//import com.warrantyclaim.warrantyclaim_api.dto.RegisterRequest;
//import com.warrantyclaim.warrantyclaim_api.dto.RegisterResponse;
//import com.warrantyclaim.warrantyclaim_api.entity.*;
//import com.warrantyclaim.warrantyclaim_api.exception.ResourceAlreadyExistsException;
//import com.warrantyclaim.warrantyclaim_api.repository.*;
//import com.warrantyclaim.warrantyclaim_api.enums.Role;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.bcrypt.BCrypt;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Set;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//public class AuthService {
//
//    private final UserRepository userRepo;
//    private final EVMStaffRepository evmStaffRepo;
//    private final SCAdminRepository scAdminRepo;
//    private final SCStaffRepository scStaffRepo;
//    private final SCTechnicianRepository scTechRepo;
//    private final AuthenticationManager authManager;
//    private final JwtService jwtService;
//
//
//
//    public AuthService(UserRepository userRepo,
//                       EVMStaffRepository evmStaffRepo,
//                       SCAdminRepository scAdminRepo,
//                       SCStaffRepository scStaffRepo,
//                       SCTechnicianRepository scTechRepo,
//                       AuthenticationManager authManager,
//                       JwtService jwtService) {
//        this.userRepo = userRepo;
//        this.evmStaffRepo = evmStaffRepo;
//        this.scAdminRepo = scAdminRepo;
//        this.scStaffRepo = scStaffRepo;
//        this.scTechRepo = scTechRepo;
//        this.authManager = authManager;
//        this.jwtService = jwtService;
//    }
//
//
//
//    // REGISTER
//    @Transactional
//    public RegisterResponse register(RegisterRequest req) {
//        if (userRepo.existsByEmail(req.getEmail())) {
//            throw new ResourceAlreadyExistsException("Email is already registered");
//        }
//
//        if (req.getPassword() == null || req.getPassword().isBlank()) {
//            throw new IllegalArgumentException("Password must not be null or empty");
//        }
//
//        // check quyen tao tk
//        if (req.getCreatedByEmail() != null && !req.getCreatedByEmail().isBlank()) {
//            User creator = userRepo.findByEmail(req.getCreatedByEmail())
//                    .orElseThrow(() -> new IllegalArgumentException("Creator not found"));
//
//            Set<Role> creatorRoles = creator.getRoles();
//            Set<Role> targetRoles = req.getRoles() != null ? req.getRoles() : Set.of(Role.SC_STAFF);
//
//
//            if (creatorRoles.contains(Role.EVM_ADMIN)) {
//                for (Role role : targetRoles) {
//                    if (role != Role.EVM_STAFF && role != Role.SC_ADMIN) {
//                        throw new IllegalArgumentException("EVM_ADMIN can only create EVM_STAFF or SC_ADMIN accounts");
//                    }
//                }
//
//            } else if (creatorRoles.contains(Role.SC_ADMIN)) {
//                if (!targetRoles.stream().allMatch(role ->
//                        role == Role.SC_STAFF || role == Role.SC_TECHNICAL)) {
//                    throw new IllegalArgumentException("SC_ADMIN can only create SC_STAFF or SC_TECHNICAL accounts");
//                }
//
//                // Kiểm tra chi nhánh phải giống nhau
//                if (!creator.getBranchOffice().equals(req.getBranchOffice())) {
//                    throw new IllegalArgumentException("SC_ADMIN can only create accounts within the same branch office");
//                }
//
//            } else {
//                throw new IllegalArgumentException("You do not have permission to create an account!");
//            }
//
//        }
//        // Tao user moi
//        User user = new User();
//        user.setUsername(req.getUsername());
//        user.setEmail(req.getEmail());
//        user.setPassword(BCrypt.hashpw(req.getPassword(), BCrypt.gensalt()));  // ma hoa mk
//        if (req.getRoles() != null && req.getRoles().stream().anyMatch(role ->
//                role == Role.SC_ADMIN || role == Role.SC_STAFF || role == Role.SC_TECHNICAL)) {
//            user.setBranchOffice(req.getBranchOffice());
//        }
//
//        user.setPhoneNumber(req.getPhoneNumber());
//        user.setDateOfBirth(req.getDateOfBirth());
//        if (req.getRoles() != null && req.getRoles().contains(Role.SC_TECHNICAL)) {
//            user.setSpecialty(req.getSpecialty());
//        }
//
//        // Gan role mac dinh or tu request
//        Set<Role> roles = req.getRoles() != null ? req.getRoles() : Set.of(Role.SC_STAFF);
//        user.setRoles(roles);
//
//        User savedUser = userRepo.save(user);
//
//        for (Role role : roles) {
//            switch (role) {
//                case EVM_STAFF -> {
//                    EVMStaff evm = new EVMStaff();
//                    evm.setId(UUID.randomUUID().toString());
//                    evm.setUserId(savedUser.getId());
//                    evm.setName(req.getUsername());
//                    evm.setEmail(req.getEmail());
//                    evm.setPassword(savedUser.getPassword());
//                    evm.setPhoneNumber(req.getPhoneNumber());
//                    evm.setDepartment(req.getBranchOffice());
//                    evm.setDateOfBirth(req.getDateOfBirth());
//                    evmStaffRepo.save(evm);
//                }
//                case SC_ADMIN -> {
//                    SCAdmin admin = new SCAdmin();
//                    admin.setId(UUID.randomUUID().toString());
//                    admin.setUserId(savedUser.getId());
//                    admin.setAccountName(req.getUsername());
//                    admin.setEmail(req.getEmail());
//                    admin.setPassword(savedUser.getPassword());
//                    admin.setPhoneNumber(req.getPhoneNumber());
//                    admin.setBranchOffice(req.getBranchOffice());
//                    admin.setDateOfBirth(req.getDateOfBirth());
//                    scAdminRepo.save(admin);
//                }
//                case SC_STAFF -> {
//                    SCStaff staff = new SCStaff();
//                    staff.setId(UUID.randomUUID().toString());
//                    staff.setUserId(savedUser.getId());
//                    staff.setAccountName(req.getUsername());
//                    staff.setEmail(req.getEmail());
//                    staff.setPassword(savedUser.getPassword());
//                    staff.setPhoneNumber(req.getPhoneNumber());
//                    staff.setDateOfBirth(req.getDateOfBirth());
//                    staff.setBranchOffice(req.getBranchOffice());
//                    scStaffRepo.save(staff);
//                }
//                case SC_TECHNICAL -> {
//                    if (req.getSpecialty() == null || req.getSpecialty().isBlank()) {
//                        throw new IllegalArgumentException("Specialty is required for SC_TECHNICAL");
//                    }
//                    SCTechnician tech = new SCTechnician();
//                    tech.setId(UUID.randomUUID().toString());
//                    tech.setUserId(savedUser.getId());
//                    tech.setName(req.getUsername());
//                    tech.setEmail(req.getEmail());
//                    tech.setPassword(savedUser.getPassword());
//                    tech.setPhoneNumber(req.getPhoneNumber());
//                    tech.setDateOfBirth(req.getDateOfBirth());
//                    tech.setSpecialty(req.getSpecialty());
//                    tech.setBranchOffice(req.getBranchOffice());
//                    scTechRepo.save(tech);
//                }
//            }
//        }
//
//    }
//
//
//            // Chuyen roles de tra ve
//            Set<String> roleNames = savedUser.getRoles().stream()
//                    .map(Enum::name)
//                    .collect(Collectors.toSet());
//
//            return new RegisterResponse(
//                    savedUser.getId(),
//                    savedUser.getUsernameDisplay(),
//                    savedUser.getEmail(),
//                    roleNames
//            );
//        }
//
//        // DELETE USER
//        @Transactional
//        public void deleteUserById (Long id){
//            if (!userRepo.existsById(id)) {
//                throw new IllegalArgumentException("User not found with id: " + id);
//            }
//            userRepo.deleteById(id);
//        }
//
//        public LoginResponse login (LoginRequest request){
//            Authentication auth = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
//            );
//
//            User user = (User) auth.getPrincipal();
//            String token = jwtService.generateToken(user);
//
//            return new LoginResponse(token, user.getUsernameDisplay(), user.getRoles());
//        }
//    }
//
//}
package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.LoginRequest;
import com.warrantyclaim.warrantyclaim_api.dto.LoginResponse;
import com.warrantyclaim.warrantyclaim_api.dto.RegisterRequest;
import com.warrantyclaim.warrantyclaim_api.dto.RegisterResponse;
import com.warrantyclaim.warrantyclaim_api.entity.*;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceAlreadyExistsException;
import com.warrantyclaim.warrantyclaim_api.repository.*;
import com.warrantyclaim.warrantyclaim_api.enums.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final EVMStaffRepository evmStaffRepo;
    private final SCAdminRepository scAdminRepo;
    private final SCStaffRepository scStaffRepo;
    private final SCTechnicianRepository scTechRepo;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepo,
                       EVMStaffRepository evmStaffRepo,
                       SCAdminRepository scAdminRepo,
                       SCStaffRepository scStaffRepo,
                       SCTechnicianRepository scTechRepo,
                       AuthenticationManager authManager,
                       JwtService jwtService) {
        this.userRepo = userRepo;
        this.evmStaffRepo = evmStaffRepo;
        this.scAdminRepo = scAdminRepo;
        this.scStaffRepo = scStaffRepo;
        this.scTechRepo = scTechRepo;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new ResourceAlreadyExistsException("Email is already registered");
        }

        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }

        if (req.getCreatedByEmail() != null && !req.getCreatedByEmail().isBlank()) {
            User creator = userRepo.findByEmail(req.getCreatedByEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Creator not found"));

            Set<Role> creatorRoles = creator.getRoles();
            Set<Role> targetRoles = req.getRoles() != null ? req.getRoles() : Set.of(Role.SC_STAFF);

            if (creatorRoles.contains(Role.EVM_ADMIN)) {
                for (Role role : targetRoles) {
                    if (role != Role.EVM_STAFF && role != Role.SC_ADMIN) {
                        throw new IllegalArgumentException("EVM_ADMIN can only create EVM_STAFF or SC_ADMIN accounts");
                    }
                }
            } else if (creatorRoles.contains(Role.SC_ADMIN)) {
                if (!targetRoles.stream().allMatch(role -> role == Role.SC_STAFF || role == Role.SC_TECHNICAL)) {
                    throw new IllegalArgumentException("SC_ADMIN can only create SC_STAFF or SC_TECHNICAL accounts");
                }

                // Sửa lỗi NullPointerException bằng cách kiểm tra null trước khi so sánh
                if (creator.getBranchOffice() == null || req.getBranchOffice() == null ||
                        !creator.getBranchOffice().equals(req.getBranchOffice())) {
                    throw new IllegalArgumentException(
                            "SC_ADMIN can only create accounts within the same branch office");
                }
            } else {
                throw new IllegalArgumentException("You do not have permission to create an account!");
            }
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(BCrypt.hashpw(req.getPassword(), BCrypt.gensalt()));

        if (req.getRoles() != null && req.getRoles().stream()
                .anyMatch(role -> role == Role.SC_ADMIN || role == Role.SC_STAFF || role == Role.SC_TECHNICAL)) {
            user.setBranchOffice(req.getBranchOffice());
        }

        user.setPhoneNumber(req.getPhoneNumber());
        user.setDateOfBirth(req.getDateOfBirth());

        if (req.getRoles() != null && req.getRoles().contains(Role.SC_TECHNICAL)) {
            user.setSpecialty(req.getSpecialty());
        }

        Set<Role> roles = req.getRoles() != null ? req.getRoles() : Set.of(Role.SC_STAFF);
        user.setRoles(roles);

        User savedUser = userRepo.save(user);

        for (Role role : roles) {
            switch (role) {
                case EVM_STAFF -> {
                    EVMStaff evm = new EVMStaff();
                    evm.setId(UUID.randomUUID().toString());
                    evm.setUserId(savedUser.getId());
                    evm.setName(req.getUsername());
                    evm.setEmail(req.getEmail());
                    evm.setPassword(savedUser.getPassword());
                    evm.setPhoneNumber(req.getPhoneNumber());
                    evm.setDepartment(req.getBranchOffice());
                    evm.setDateOfBirth(req.getDateOfBirth());
                    evmStaffRepo.save(evm);
                }
                case SC_ADMIN -> {
                    SCAdmin admin = new SCAdmin();
                    admin.setId(UUID.randomUUID().toString());
                    admin.setUserId(savedUser.getId());
                    admin.setAccountName(req.getUsername());
                    admin.setEmail(req.getEmail());
                    admin.setPassword(savedUser.getPassword());
                    admin.setPhoneNumber(req.getPhoneNumber());
                    admin.setBranchOffice(req.getBranchOffice());
                    admin.setDateOfBirth(req.getDateOfBirth());
                    scAdminRepo.save(admin);
                }
                case SC_STAFF -> {
                    SCStaff staff = new SCStaff();
                    staff.setId(UUID.randomUUID().toString());
                    staff.setUserId(savedUser.getId());
                    staff.setAccountName(req.getUsername());
                    staff.setEmail(req.getEmail());
                    staff.setPassword(savedUser.getPassword());
                    staff.setPhoneNumber(req.getPhoneNumber());
                    staff.setDateOfBirth(req.getDateOfBirth());
                    staff.setBranchOffice(req.getBranchOffice());
                    scStaffRepo.save(staff);
                }
                case SC_TECHNICAL -> {
                    if (req.getSpecialty() == null || req.getSpecialty().isBlank()) {
                        throw new IllegalArgumentException("Specialty is required for SC_TECHNICAL");
                    }
                    SCTechnician tech = new SCTechnician();
                    tech.setId(UUID.randomUUID().toString());
                    tech.setUserId(savedUser.getId());
                    tech.setName(req.getUsername());
                    tech.setEmail(req.getEmail());
                    tech.setPassword(savedUser.getPassword());
                    tech.setPhoneNumber(req.getPhoneNumber());
                    tech.setDateOfBirth(req.getDateOfBirth());
                    tech.setSpecialty(req.getSpecialty());
                    tech.setBranchOffice(req.getBranchOffice());
                    scTechRepo.save(tech);
                }
            }
        }

        Set<String> roleNames = savedUser.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsernameDisplay(),
                savedUser.getEmail(),
                roleNames);
    }


    // DELETE USER
    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepo.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepo.deleteById(id);
    }

    // LOGIN
    public LoginResponse login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user);

        return new LoginResponse(token, user.getId(), user.getUsernameDisplay(), user.getRoles(),
                user.getBranchOffice());
    }
}
