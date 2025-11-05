package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.ChangePasswordRequest;
import com.warrantyclaim.warrantyclaim_api.dto.UpdateUserRequest;
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
            @AuthenticationPrincipal User user
    ) {
        UserResponse res = userService.updateUser(req, user.getEmail());
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", res));
    }


    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal User user
    ) {
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
            @AuthenticationPrincipal User requester
    ) {
        UserResponse user = userService.findEvmStaffById(id, requester.getEmail());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/sc-users")
    public ResponseEntity<List<UserResponse>> getSCUsers(
            @AuthenticationPrincipal User requester
    ) {
        List<UserResponse> users = userService.findSCUsersByBranch(requester.getEmail());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @AuthenticationPrincipal User requester
    ) {
        if (!requester.getRoles().contains(Role.EVM_ADMIN)) {
            return ResponseEntity.status(403).body(null); // hoặc dùng ApiResponse.error(...) nếu có wrapper
        }

        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/evm-admin/users-by-branch")
    public ResponseEntity<List<UserResponse>> getUsersByBranchForEvmAdmin(
            @AuthenticationPrincipal User requester,
            @RequestParam String branchOffice
    ) {
        List<UserResponse> users = userService.findUsersByBranchForEvmAdmin(requester.getEmail(), branchOffice);
        return ResponseEntity.ok(users);
    }



}
