package com.shopsphere.auth.service;

import com.shopsphere.auth.dto.*;
import com.shopsphere.auth.entity.Role;
import com.shopsphere.auth.entity.TokenBlacklist;
import com.shopsphere.auth.entity.User;
import com.shopsphere.auth.exception.ConflictException;
import com.shopsphere.auth.exception.ResourceNotFoundException;
import com.shopsphere.auth.exception.UnauthorizedException;
import com.shopsphere.auth.repository.TokenBlacklistRepository;
import com.shopsphere.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenBlacklistRepository blacklistRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ConflictException("Email already registered");
        }
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.CUSTOMER)
                .isActive(true)
                .build();
        userRepository.save(user);
        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new UnauthorizedException("Account disabled");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        return buildAuthResponse(user);
    }

    public UserDto getProfile(String email) {
        return UserDto.from(loadUser(email));
    }

    @Transactional
    public UserDto updateProfile(String email, UpdateProfileRequest req) {
        User user = loadUser(email);
        user.setName(req.getName());
        return UserDto.from(user);
    }

    @Transactional
    public void logout(String token) {
        if (token == null || token.isBlank()) return;
        Date exp = jwtService.extractAllClaims(token).getExpiration();
        LocalDateTime expiresAt = Instant.ofEpochMilli(exp.getTime())
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        blacklistRepository.save(TokenBlacklist.builder()
                .tokenHash(Integer.toHexString(token.hashCode()))
                .expiresAt(expiresAt)
                .build());
    }

    public AuthResponse refresh(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("Missing refresh token");
        }
        try {
            String email = jwtService.extractEmail(token);
            User user = loadUser(email);
            return buildAuthResponse(user);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid refresh token");
        }
    }

    private User loadUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
