package com.example.demo.Controller;

import com.example.demo.Config.AuthUtil;
import com.example.demo.Exception.BankingException;
import com.example.demo.Model.Accounts;
import com.example.demo.Repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AuthUtil authUtil;

    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountController = new AccountController(accountRepository, authUtil);
    }

    @Test
    void allowsAccessToOwnAccount() throws Exception {
        Accounts account = new Accounts();
        account.setAccountId(1);
        account.setCustomerId(5);

        when(accountRepository.findById(1)).thenReturn(account);
        when(authUtil.getCurrentCustomerId()).thenReturn(5);

        Accounts result = accountController.getAccount(1);

        assertEquals(1, result.getAccountId());
    }

    @Test
    void blocksAccessToOtherCustomersAccount() throws Exception {
        Accounts account = new Accounts();
        account.setAccountId(1);
        account.setCustomerId(5);

        when(accountRepository.findById(1)).thenReturn(account);
        when(authUtil.getCurrentCustomerId()).thenReturn(99); // different customer

        BankingException ex = assertThrows(BankingException.class,
                () -> accountController.getAccount(1));

        assertEquals(403, ex.getStatusCode());
    }

    @Test
    void returns404WhenAccountDoesNotExist() throws Exception {
        when(accountRepository.findById(999)).thenReturn(null);
        when(authUtil.getCurrentCustomerId()).thenReturn(5);

        BankingException ex = assertThrows(BankingException.class,
                () -> accountController.getAccount(999));

        assertEquals(404, ex.getStatusCode());
    }
}