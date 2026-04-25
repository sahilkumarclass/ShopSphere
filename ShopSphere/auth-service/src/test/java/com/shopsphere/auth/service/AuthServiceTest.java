package com.shopsphere.auth.service;

import com.shopsphere.auth.dto.AuthResponse;
import com.shopsphere.auth.dto.LoginRequest;
import com.shopsphere.auth.dto.SignupRequest;
import com.shopsphere.auth.entity.Role;
import com.shopsphere.auth.entity.User;
import com.shopsphere.auth.exception.ConflictException;
import com.shopsphere.auth.exception.UnauthorizedException;
import com.shopsphere.auth.repository.TokenBlacklistRepository;
import com.shopsphere.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private TokenBlacklistRepository blacklistRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks private AuthService authService;

    @Test
    void signup_createsUserAndReturnsToken() {
        SignupRequest req = new SignupRequest("Alice", "alice@test.com", "pass123");
        when(userRepository.existsByEmail("alice@test.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("$2a$encoded");
        when(jwtService.generateToken(any(User.class))).thenReturn("mock.jwt.token");

        AuthResponse resp = authService.signup(req);

        assertNotNull(resp.getToken());
        assertEquals("CUSTOMER", resp.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void signup_throwsConflict_whenEmailExists() {
        SignupRequest req = new SignupRequest("Bob", "bob@test.com", "pass123");
        when(userRepository.existsByEmail("bob@test.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.signup(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_returnsTokenWhenCredentialsMatch() {
        User user = User.builder()
                .id(1L).email("alice@test.com").password("$2a$encoded")
                .role(Role.CUSTOMER).isActive(true).name("Alice").build();
        when(userRepository.findByEmail("alice@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "$2a$encoded")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("mock.jwt");

        AuthResponse resp = authService.login(new LoginRequest("alice@test.com", "pass123"));

        assertEquals("mock.jwt", resp.getToken());
        assertEquals("CUSTOMER", resp.getRole());
    }

    @Test
    void login_throwsUnauthorized_whenPasswordWrong() {
        User user = User.builder()
                .email("alice@test.com").password("$2a$encoded")
                .role(Role.CUSTOMER).isActive(true).name("Alice").build();
        when(userRepository.findByEmail("alice@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "$2a$encoded")).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> authService.login(new LoginRequest("alice@test.com", "wrong")));
    }

    @Test
    void login_throwsUnauthorized_whenUserMissing() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());
        assertThrows(UnauthorizedException.class,
                () -> authService.login(new LoginRequest("missing@test.com", "x")));
    }

    @Test
    void login_throwsUnauthorized_whenAccountDisabled() {
        User user = User.builder()
                .email("a@test.com").password("$").role(Role.CUSTOMER).isActive(false).name("A").build();
        when(userRepository.findByEmail("a@test.com")).thenReturn(Optional.of(user));
        assertThrows(UnauthorizedException.class,
                () -> authService.login(new LoginRequest("a@test.com", "x")));
    }
}
