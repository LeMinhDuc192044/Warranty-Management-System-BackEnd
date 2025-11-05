package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.ChangePasswordRequest;
import com.warrantyclaim.warrantyclaim_api.dto.UpdateUserRequest;
import com.warrantyclaim.warrantyclaim_api.dto.UpdateUserStatusRequest;
import com.warrantyclaim.warrantyclaim_api.dto.UserResponse;
import com.warrantyclaim.warrantyclaim_api.entity.User;
import com.warrantyclaim.warrantyclaim_api.enums.Role;
import com.warrantyclaim.warrantyclaim_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @Valid @RequestBody UpdateUserRequest req,
            @RequestHeader("Authorization") String token) {
        UserResponse res = userService.updateUser(req, token);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", res));
    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> adminUpdateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest req,
            @AuthenticationPrincipal User admin) {
        if (!admin.getRoles().contains(Role.EVM_ADMIN)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Chỉ EVM_ADMIN mới có quyền cập nhật người dùng khác"));
        }

        UserResponse res = userService.adminUpdateUser(userId, req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật người dùng thành công", res));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal User user) {
        userService.changePassword(user.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công", null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user // dùng entity trực tiếp
    ) {
        userService.deleteUserByIdWithRoleCheck(user.getEmail(), id);
        return ResponseEntity.ok(ApiResponse.success("Xóa tài khoản thành công", null));
    }

    @GetMapping("/evm-staff/{id}")
    public ResponseEntity<UserResponse> getEvmStaffById(
            @PathVariable Long id,
            @AuthenticationPrincipal User requester) {
        UserResponse user = userService.findEvmStaffById(id, requester.getEmail());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/sc-users")
    public ResponseEntity<List<UserResponse>> getSCUsers(
            @AuthenticationPrincipal User requester) {
        List<UserResponse> users = userService.findSCUsersByBranch(requester.getEmail());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @AuthenticationPrincipal User requester) {
        if (!requester.getRoles().contains(Role.EVM_ADMIN)) {
            return ResponseEntity.status(403).body(null);
        }

        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Cập nhật trạng thái user - CHỈ EVM_ADMIN
    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequest request,
            @AuthenticationPrincipal User admin) {

        // Kiểm tra quyền EVM_ADMIN
        if (!admin.getRoles().contains(Role.EVM_ADMIN)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Chỉ EVM_ADMIN mới có quyền thay đổi trạng thái người dùng"));
        }

        try {
            UserResponse res = userService.updateUserStatus(userId, request, admin);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái thành công", res));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

}
