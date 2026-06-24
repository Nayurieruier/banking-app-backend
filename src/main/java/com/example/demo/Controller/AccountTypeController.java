package com.example.demo.Controller;

import com.example.demo.Model.AccountType;
import com.example.demo.Repository.AccountTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/account-types")
public class AccountTypeController {

    private final AccountTypeRepository accountTypeRepository;

    public AccountTypeController(AccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    @GetMapping("/{accountTypeId}")
    public AccountType getAccountType(@PathVariable int accountTypeId) throws SQLException {
        return accountTypeRepository.findById(accountTypeId);
    }

    @GetMapping
    public List<AccountType> getAllAccountTypes() throws SQLException {
        return accountTypeRepository.findAll();
    }

    @PostMapping
    public int createAccountType(@RequestBody AccountType accountType) throws SQLException {
        return accountTypeRepository.save(accountType);
    }
}