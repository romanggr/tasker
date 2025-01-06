package org.example.tasker_back.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    private JwtService jwtService;

    private final String email = "test@example.com";
    private final String secretKey = "testSecretKey";
    private String validToken;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);
        validToken = jwtService.generateToken(email);
    }


    @Test
    void testGenerateToken_success() {
        String generatedToken = jwtService.generateToken(email);

        assertNotNull(generatedToken);
        String[] parts = generatedToken.split("\\.");
        assertEquals(3, parts.length);  // Header, Payload, Signature
    }

    @Test
    void testIsTokenValid_success() {
        boolean isValid = jwtService.isTokenValid(validToken, email);
        assertTrue(isValid);
    }

    @Test
    void testIsTokenValid_whenExpired() {
        String expiredToken = jwtService.generateToken(email, 0);

        boolean isValid = jwtService.isTokenValid(expiredToken, email);
        assertFalse(isValid);
    }

    @Test
    void testIsTokenValid_whenInvalidEmail() {
        boolean isValid = jwtService.isTokenValid(validToken, "wrong@example.com");
        assertFalse(isValid);
    }
}
