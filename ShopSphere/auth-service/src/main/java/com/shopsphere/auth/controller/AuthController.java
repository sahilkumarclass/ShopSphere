package com.shopsphere.auth.controller;

import com.shopsphere.auth.dto.*;
import com.shopsphere.auth.repository.UserRepository;
import com.shopsphere.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and user profile endpoints")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    @Operation(summary = "Register a new customer")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(req));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate and receive a JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Issue a fresh JWT from a still-valid existing token")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        String token = (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
        return ResponseEntity.ok(authService.refresh(token));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user's profile")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(authService.getProfile(user.getUsername()));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current authenticated user's profile")
    public ResponseEntity<UserDto> updateMe(@AuthenticationPrincipal UserDetails user,
                                            @Valid @RequestBody UpdateProfileRequest req) {
        return ResponseEntity.ok(authService.updateProfile(user.getUsername(), req));
    }

    @PostMapping("/logout")
    @Operation(summary = "Invalidate the current bearer token")
    public ResponseEntity<Void> logout(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            authService.logout(header.substring(7));
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[Admin] List all registered users")
    public ResponseEntity<List<UserDto>> listUsers() {
        return ResponseEntity.ok(userRepository.findAll().stream().map(UserDto::from).toList());
    }
}
