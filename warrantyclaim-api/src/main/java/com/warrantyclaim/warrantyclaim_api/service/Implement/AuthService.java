//package com.warrantyclaim.warrantyclaim_api.service.Implement;
//
//
//import com.warrantyclaim.warrantyclaim_api.dto.RegisterRequest;
//import com.warrantyclaim.warrantyclaim_api.dto.RegisterResponse;
//
//import com.warrantyclaim.warrantyclaim_api.entity.User;
//import com.warrantyclaim.warrantyclaim_api.enums.Role;
//import com.warrantyclaim.warrantyclaim_api.exception.ResourceAlreadyExistsException;
//import com.warrantyclaim.warrantyclaim_api.repository.UserRepository;
//
//import org.springframework.security.crypto.bcrypt.BCrypt;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public class AuthService {
//
//    private final UserRepository userRepo;
//
//    public AuthService(UserRepository userRepo) {
//        this.userRepo = userRepo;
//    }
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
//            // if (creatorRoles.contains(Role.EVM_ADMIN)) {
//            //   if (!targetRoles.stream().allMatch(role ->
//            //   // role == Role.EVM_STAFF || role == Role.SC_ADMIN)) {
//            //     role.equals(Role.EVM_STAFF) || role.equals(Role.SC_ADMIN))) {
//            //  throw new IllegalArgumentException("EVM_ADMIN chỉ được tạo EVM_STAFF hoặc SC_ADMIN");
//            if (creatorRoles.contains(Role.EVM_ADMIN)) {
//                for (Role role : targetRoles) {
//                    if (role != Role.EVM_STAFF && role != Role.SC_ADMIN) {
//                        throw new IllegalArgumentException("EVM_ADMIN can only create EVM_STAFF or SC_ADMIN accounts");
//                    }
//                }
//
//
//            } else if (creatorRoles.contains(Role.SC_ADMIN)) {
//                if (!targetRoles.stream().allMatch(role ->
//                        role == Role.SC_STAFF || role == Role.SC_TECHNICAL)) {
//                    throw new IllegalArgumentException("SC_ADMIN can only create SC_STAFF or SC_TECHNICAL accounts");
//                }
//            } else {
//                throw new IllegalArgumentException("You do not have permission to create an account!");
//            }
//        }
//
//        // Tao user moi
//        User user = new User();
//        user.setUsername(req.getUsername());
//        user.setEmail(req.getEmail());
//        user.setPassword(BCrypt.hashpw(req.getPassword(), BCrypt.gensalt()));  // ma hoa mk
//
//        // Gan role mac dinh or tu request
//        Set<Role> roles = req.getRoles() != null ? req.getRoles() : Set.of(Role.SC_STAFF);
//        user.setRoles(roles);
//
//        User savedUser = userRepo.save(user);
//
//        // Chuyen roles de tra ve
//        Set<String> roleNames = savedUser.getRoles().stream()
//                .map(Enum::name)
//                .collect(Collectors.toSet());
//
//        return new RegisterResponse(
//                savedUser.getId(),
//                savedUser.getUsernameDisplay(),
//                savedUser.getEmail(),
//                roleNames
//        );
//    }
//
//    // DELETE USER
//    @Transactional
//    public void deleteUserById(Long id) {
//        if (!userRepo.existsById(id)) {
//            throw new IllegalArgumentException("User not found with id: " + id);
//        }
//        userRepo.deleteById(id);
//    }
//}
