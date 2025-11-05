package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.ChangePasswordRequest;
import com.warrantyclaim.warrantyclaim_api.dto.UpdateUserRequest;
import com.warrantyclaim.warrantyclaim_api.dto.UpdateUserStatusRequest;
import com.warrantyclaim.warrantyclaim_api.dto.UserResponse;
import com.warrantyclaim.warrantyclaim_api.entity.SCStaff;
import com.warrantyclaim.warrantyclaim_api.entity.SCAdmin;
import com.warrantyclaim.warrantyclaim_api.entity.SCTechnician;
import com.warrantyclaim.warrantyclaim_api.entity.User;
import com.warrantyclaim.warrantyclaim_api.enums.Role;
import com.warrantyclaim.warrantyclaim_api.repository.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final SCStaffRepository scStaffRepo;
    private final SCTechnicianRepository scTechRepo;
    private final SCAdminRepository scAdminRepo;
    private final EVMStaffRepository evmStaffRepo;

    public UserService(UserRepository userRepo,
            JwtService jwtService,
            SCStaffRepository scStaffRepo,
            SCTechnicianRepository scTechRepo,
            SCAdminRepository scAdminRepo,
            EVMStaffRepository evmStaffRepo) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.scStaffRepo = scStaffRepo;
        this.scTechRepo = scTechRepo;
        this.scAdminRepo = scAdminRepo;
        this.evmStaffRepo = evmStaffRepo;
    }
// SỬA LẠI CHỔ NÀY INJEC USER THÔNG QUA AuthenticationPrincipal
    @Transactional
//    public UserResponse updateUser(UpdateUserRequest req, String token) {
//        String emailFromToken = jwtService.extractUserName(token.replace("Bearer ", ""));
//        User user = userRepo.findByEmail(emailFromToken)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));

    public UserResponse updateUser(UpdateUserRequest req, String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setDateOfBirth(req.getDateOfBirth());

        if (user.getRoles().stream()
                .anyMatch(role -> role == Role.SC_ADMIN || role == Role.SC_STAFF || role == Role.SC_TECHNICAL)) {
            user.setBranchOffice(req.getBranchOffice());
        }

        if (user.getRoles().contains(Role.SC_TECHNICAL)) {
            user.setSpecialty(req.getSpecialty());
        }

        User updatedUser = userRepo.save(user);

        for (Role role : updatedUser.getRoles()) {
            switch (role) {
                case EVM_ADMIN:
                    // EVM_ADMIN không cần update bảng khác
                    break;
                case SC_STAFF:
                    scStaffRepo.findByEmail(emailFromToken)
                            .ifPresent(staff -> {
                                staff.setAccountName(req.getUsername());
                                staff.setEmail(req.getEmail());
                                staff.setPhoneNumber(req.getPhoneNumber());
                                staff.setDateOfBirth(req.getDateOfBirth());
                                staff.setBranchOffice(req.getBranchOffice());
                                scStaffRepo.save(staff);
                            });
                    break;
                case SC_TECHNICAL:
                    scTechRepo.findByEmail(emailFromToken)
                            .ifPresent(tech -> {
                                tech.setName(req.getUsername());
                                tech.setEmail(req.getEmail());
                                tech.setPhoneNumber(req.getPhoneNumber());
                                tech.setDateOfBirth(req.getDateOfBirth());
                                tech.setBranchOffice(req.getBranchOffice());
                                tech.setSpecialty(req.getSpecialty());
                                scTechRepo.save(tech);
                            });
                    break;
                case SC_ADMIN:
                    scAdminRepo.findByEmail(emailFromToken)
                            .ifPresent(admin -> {
                                admin.setAccountName(req.getUsername());
                                admin.setEmail(req.getEmail());
                                admin.setPhoneNumber(req.getPhoneNumber());
                                admin.setDateOfBirth(req.getDateOfBirth());
                                admin.setBranchOffice(req.getBranchOffice());
                                scAdminRepo.save(admin);
                            });
                    break;
                case EVM_STAFF:
                    evmStaffRepo.findByEmail(emailFromToken)
                            .ifPresent(evm -> {
                                evm.setName(req.getUsername());
                                evm.setEmail(req.getEmail());
                                evm.setPhoneNumber(req.getPhoneNumber());
                                evm.setDateOfBirth(req.getDateOfBirth());
                                evmStaffRepo.save(evm);
                            });
            }
        }

        return toUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse adminUpdateUser(Long userId, UpdateUserRequest req) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        // Không cho phép update EVM_ADMIN
        if (user.getRoles().contains(Role.EVM_ADMIN)) {
            throw new IllegalArgumentException("Không thể cập nhật thông tin EVM_ADMIN");
        }

        // Validate branchOffice cho SC roles
        boolean isSCRole = user.getRoles().stream()
                .anyMatch(role -> role == Role.SC_ADMIN || role == Role.SC_STAFF || role == Role.SC_TECHNICAL);

        if (isSCRole && (req.getBranchOffice() == null || req.getBranchOffice().trim().isEmpty())) {
            throw new IllegalArgumentException("Branch office là bắt buộc cho vai trò SC");
        }

        String oldEmail = user.getEmail();

        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setDateOfBirth(req.getDateOfBirth());

        // Chỉ SC roles mới có branchOffice
        if (isSCRole) {
            user.setBranchOffice(req.getBranchOffice());
        }

        if (user.getRoles().contains(Role.SC_TECHNICAL)) {
            user.setSpecialty(req.getSpecialty());
        }

        User updatedUser = userRepo.save(user);

        // Cập nhật bảng phụ sử dụng email cũ
        for (Role role : updatedUser.getRoles()) {
            switch (role) {
                case EVM_ADMIN:
                    // EVM_ADMIN không cần update bảng khác
                    break;
                case SC_STAFF:
                    scStaffRepo.findByEmail(oldEmail)
                            .ifPresent(staff -> {
                                staff.setAccountName(req.getUsername());
                                staff.setEmail(req.getEmail());
                                staff.setPhoneNumber(req.getPhoneNumber());
                                staff.setDateOfBirth(req.getDateOfBirth());
                                staff.setBranchOffice(req.getBranchOffice());
                                scStaffRepo.save(staff);
                            });
                    break;
                case SC_TECHNICAL:
                    scTechRepo.findByEmail(oldEmail)
                            .ifPresent(tech -> {
                                tech.setName(req.getUsername());
                                tech.setEmail(req.getEmail());
                                tech.setPhoneNumber(req.getPhoneNumber());
                                tech.setDateOfBirth(req.getDateOfBirth());
                                tech.setBranchOffice(req.getBranchOffice());
                                tech.setSpecialty(req.getSpecialty());
                                scTechRepo.save(tech);
                            });
                    break;
                case SC_ADMIN:
                    scAdminRepo.findByEmail(oldEmail)
                            .ifPresent(admin -> {
                                admin.setAccountName(req.getUsername());
                                admin.setEmail(req.getEmail());
                                admin.setPhoneNumber(req.getPhoneNumber());
                                admin.setDateOfBirth(req.getDateOfBirth());
                                admin.setBranchOffice(req.getBranchOffice());
                                scAdminRepo.save(admin);
                            });
                    break;
                case EVM_STAFF:
                    evmStaffRepo.findByEmail(oldEmail)
                            .ifPresent(evm -> {
                                evm.setName(req.getUsername());
                                evm.setEmail(req.getEmail());
                                evm.setPhoneNumber(req.getPhoneNumber());
                                evm.setDateOfBirth(req.getDateOfBirth());
                                evmStaffRepo.save(evm);
                            });
                    break;
            }
        }

        return toUserResponse(updatedUser);
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!BCrypt.checkpw(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        String hashed = BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        userRepo.save(user);

        for (Role role : user.getRoles()) {
            switch (role) {
                case EVM_ADMIN:
                    // EVM_ADMIN không cần update bảng khác
                    break;
                case SC_STAFF:
                    scStaffRepo.findByEmail(email)
                            .ifPresent(staff -> {
                                staff.setPassword(hashed);
                                scStaffRepo.save(staff);
                            });
                    break;
                case SC_TECHNICAL:
                    scTechRepo.findByEmail(email)
                            .ifPresent(tech -> {
                                tech.setPassword(hashed);
                                scTechRepo.save(tech);
                            });
                    break;
                case SC_ADMIN:
                    scAdminRepo.findByEmail(email)
                            .ifPresent(admin -> {
                                admin.setPassword(hashed);
                                scAdminRepo.save(admin);
                            });
                    break;
                case EVM_STAFF:
                    evmStaffRepo.findByEmail(email)
                            .ifPresent(evm -> {
                                evm.setPassword(hashed);
                                evmStaffRepo.save(evm);
                            });
                    break;
            }
        }
    }

    @Transactional
    public void deleteUserByIdWithRoleCheck(String requesterEmail, Long targetUserId) {
        User requester = userRepo.findByEmail(requesterEmail)
                .orElseThrow(() -> new IllegalArgumentException("Người gọi không tồn tại"));

        User target = userRepo.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Người bị xóa không tồn tại"));

        Set<String> requesterRoles = requester.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<Role> targetRoles = target.getRoles();

        if (requesterRoles.contains("EVM_ADMIN")) {
            if (targetRoles.contains(Role.EVM_ADMIN)) {
                throw new RuntimeException("Không thể xóa EVM_ADMIN khác");
            }
        } else if (requesterRoles.contains("SC_ADMIN")) {
            for (Role role : targetRoles) {
                if (role != Role.SC_STAFF && role != Role.SC_TECHNICAL) {
                    throw new RuntimeException("SC_ADMIN chỉ được xóa SC_STAFF hoặc SC_TECHNICAL");
                }

                String requesterBranch = scAdminRepo.findByEmail(requester.getEmail())
                        .map(SCAdmin::getBranchOffice)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin SC_ADMIN"));

                String targetBranch = switch (role) {
                    case SC_STAFF -> scStaffRepo.findByEmail(target.getEmail())
                            .map(SCStaff::getBranchOffice)
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy SC_STAFF để kiểm tra chi nhánh"));
                    case SC_TECHNICAL -> scTechRepo.findByEmail(target.getEmail())
                            .map(SCTechnician::getBranchOffice)
                            .orElseThrow(
                                    () -> new RuntimeException("Không tìm thấy SC_TECHNICAL để kiểm tra chi nhánh"));
                    default -> throw new RuntimeException("Không hỗ trợ kiểm tra chi nhánh cho role này");
                };

                if (!requesterBranch.equalsIgnoreCase(targetBranch)) {
                    throw new RuntimeException("SC_ADMIN chỉ được xóa người cùng chi nhánh");
                }
            }
        } else {
            throw new RuntimeException("Bạn không có quyền xóa tài khoản");
        }

        for (Role role : targetRoles) {
            switch (role) {
                case EVM_ADMIN:
                    // EVM_ADMIN không có bảng phụ để xóa
                    break;
                case SC_STAFF:
                    scStaffRepo.deleteByEmail(target.getEmail());
                    break;
                case SC_TECHNICAL:
                    scTechRepo.deleteByEmail(target.getEmail());
                    break;
                case SC_ADMIN:
                    scAdminRepo.deleteByEmail(target.getEmail());
                    break;
                case EVM_STAFF:
                    evmStaffRepo.deleteByEmail(target.getEmail());
                    break;
            }
        }

        userRepo.deleteById(targetUserId);
    }

    public UserResponse findEvmStaffById(Long id, String requesterEmail) {
        User requester = userRepo.findByEmail(requesterEmail)
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));

        if (!requester.getRoles().contains(Role.EVM_ADMIN)) {
            throw new IllegalArgumentException("Only EVM_ADMIN can access this resource");
        }

        User target = userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!target.getRoles().contains(Role.EVM_STAFF)) {
            throw new IllegalArgumentException("Target is not EVM_STAFF");
        }

        return toUserResponse(target);
    }

    public List<UserResponse> findSCUsersByBranch(String requesterEmail) {
        User requester = userRepo.findByEmail(requesterEmail)
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));

        Set<Role> roles = requester.getRoles();
        List<User> users;

        if (roles.contains(Role.EVM_ADMIN)) {
            users = userRepo
                    .findByRolesIn(new HashSet<>(Arrays.asList(Role.SC_ADMIN, Role.SC_STAFF, Role.SC_TECHNICAL)));
        } else if (roles.contains(Role.SC_ADMIN)) {
            // SC_ADMIN: Lấy SC_STAFF và SC_TECHNICAL trong cùng chi nhánh
            users = userRepo.findByBranchOfficeAndRolesIn(
                    requester.getBranchOffice(),
                    new HashSet<>(Arrays.asList(Role.SC_STAFF, Role.SC_TECHNICAL)));
            // Thêm chính SC_ADMIN vào danh sách để frontend có thể lấy branchOffice
            users.add(0, requester);
        } else if (roles.contains(Role.SC_STAFF)) {
            users = userRepo.findByBranchOfficeAndRolesIn(
                    requester.getBranchOffice(),
                    new HashSet<>(Arrays.asList(Role.SC_TECHNICAL)));
        } else {
            throw new IllegalArgumentException("You do not have permission to view SC users");
        }

        return users.stream().map(this::toUserResponse).collect(Collectors.toList());
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream().map(this::toUserResponse).collect(Collectors.toList());
    }

    // ✅ Chuyển đổi từ User → UserResponse
    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsernameDisplay(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBranchOffice(),
                user.getDateOfBirth(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()),
                user.getAccountStatus());

        // Add status change metadata
        response.setStatusChangedAt(user.getStatusChangedAt());
        response.setStatusChangedBy(user.getStatusChangedBy());
        response.setStatusChangeReason(user.getStatusChangeReason());

        return response;
    }

    // Cập nhật trạng thái user - CHỈ EVM_ADMIN
    @Transactional
    public UserResponse updateUserStatus(Long userId, UpdateUserStatusRequest request, User admin) {
        // Kiểm tra quyền: Chỉ EVM_ADMIN mới được phép
        if (!admin.getRoles().contains(Role.EVM_ADMIN)) {
            throw new IllegalArgumentException("Chỉ EVM_ADMIN mới có quyền thay đổi trạng thái người dùng");
        }

        User targetUser = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        // Không cho phép thay đổi trạng thái EVM_ADMIN
        if (targetUser.getRoles().contains(Role.EVM_ADMIN)) {
            throw new IllegalArgumentException("Không thể thay đổi trạng thái của EVM_ADMIN");
        }

        // Không cho phép admin tự thay đổi trạng thái chính mình
        if (targetUser.getId().equals(admin.getId())) {
            throw new IllegalArgumentException("Không thể thay đổi trạng thái của chính mình");
        }

        // Cập nhật trạng thái
        targetUser.setAccountStatus(request.getStatus());
        targetUser.setStatusChangedAt(LocalDateTime.now());
        targetUser.setStatusChangedBy(admin.getId());
        targetUser.setStatusChangeReason(request.getReason());

        User updatedUser = userRepo.save(targetUser);
        return toUserResponse(updatedUser);
    }

}
