package com.shopsphere.auth.service;

import com.shopsphere.auth.entity.Role;
import com.shopsphere.auth.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
                "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtbXVzdC1iZS1hdC1sZWFzdC0yNTYtYml0cy1sb25nLWVub3VnaA==");
        ReflectionTestUtils.setField(jwtService, "expiration", 3_600_000L);
        ReflectionTestUtils.invokeMethod(jwtService, "init");
    }

    @Test
    void generateAndExtractToken_roundTrip() {
        User user = User.builder().id(7L).email("zed@test.com").role(Role.ADMIN).name("Zed").build();
        String token = jwtService.generateToken(user);

        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("zed@test.com", claims.getSubject());
        assertEquals("ADMIN", claims.get("role"));
        assertEquals(7, ((Number) claims.get("userId")).longValue());
    }

    @Test
    void isTokenValid_returnsTrueForMatchingUser() {
        User user = User.builder().id(1L).email("a@b.com").role(Role.CUSTOMER).name("A").build();
        String token = jwtService.generateToken(user);

        UserDetails details = stubDetails("a@b.com");
        assertTrue(jwtService.isTokenValid(token, details));
    }

    @Test
    void isTokenValid_returnsFalseForDifferentUser() {
        User user = User.builder().id(1L).email("a@b.com").role(Role.CUSTOMER).name("A").build();
        String token = jwtService.generateToken(user);

        UserDetails details = stubDetails("c@d.com");
        assertFalse(jwtService.isTokenValid(token, details));
    }

    private UserDetails stubDetails(final String username) {
        return new UserDetails() {
            @Override public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
                return Collections.emptyList();
            }
            @Override public String getPassword() { return ""; }
            @Override public String getUsername() { return username; }
            @Override public boolean isAccountNonExpired() { return true; }
            @Override public boolean isAccountNonLocked() { return true; }
            @Override public boolean isCredentialsNonExpired() { return true; }
            @Override public boolean isEnabled() { return true; }
        };
    }
}
