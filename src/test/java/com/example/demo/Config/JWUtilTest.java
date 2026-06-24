package com.example.demo.Config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void generatedTokenIsValid() {
        String token = jwtUtil.generateToken(1, "test@example.com");
        assertTrue(jwtUtil.isTokenValid(token));
    }

    @Test
    void canExtractEmailFromToken() {
        String token = jwtUtil.generateToken(1, "test@example.com");
        assertEquals("test@example.com", jwtUtil.extractEmail(token));
    }

    @Test
    void canExtractCustomerIdFromToken() {
        String token = jwtUtil.generateToken(42, "test@example.com");
        assertEquals(42, jwtUtil.extractCustomerId(token));
    }

    @Test
    void tamperedTokenIsInvalid() {
        String token = jwtUtil.generateToken(1, "test@example.com");
        String tampered = token.substring(0, token.length() - 5) + "xxxxx";
        assertFalse(jwtUtil.isTokenValid(tampered));
    }

    @Test
    void malformedTokenIsInvalid() {
        assertFalse(jwtUtil.isTokenValid("not.a.real.token"));
    }
}