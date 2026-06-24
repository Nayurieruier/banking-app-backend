package com.example.demo.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankingExceptionTest {

    @Test
    void carriesMessageAndStatusCode() {
        BankingException ex = new BankingException("Insufficient funds.", 409);

        assertEquals("Insufficient funds.", ex.getMessage());
        assertEquals(409, ex.getStatusCode());
    }

    @Test
    void isARuntimeException() {
        BankingException ex = new BankingException("Test", 400);
        assertTrue(ex instanceof RuntimeException);
    }
}