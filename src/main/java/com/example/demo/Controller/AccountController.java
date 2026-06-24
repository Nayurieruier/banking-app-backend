package com.example.demo.Controller;

import com.example.demo.Config.AuthUtil;
import com.example.demo.DTO.TransferRequest;
import com.example.demo.Exception.BankingException;
import com.example.demo.Model.Accounts;
import com.example.demo.Repository.AccountRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;

    public AccountController(AccountRepository accountRepository, AuthUtil authUtil) {
        this.accountRepository = accountRepository;
        this.authUtil = authUtil;
    }

    @GetMapping("/{accountId}")
    public Accounts getAccount(@PathVariable long accountId) throws SQLException {
        Accounts account = accountRepository.findById(accountId);
        checkOwnership(account);
        return account;
    }

    @GetMapping("/{accountId}/balance")
    public BigDecimal getBalance(@PathVariable long accountId) throws SQLException {
        Accounts account = accountRepository.findById(accountId);
        checkOwnership(account);
        return account.getBalance();
    }

    @GetMapping("/customer/{customerId}")
    public List<Accounts> getAccountsForCustomer(@PathVariable int customerId) throws SQLException {
        if (customerId != authUtil.getCurrentCustomerId()) {
            throw new BankingException("You can only view your own accounts.", 403);
        }
        return accountRepository.findByCustomerId(customerId);
    }

    @PostMapping
    public long createAccount(@RequestBody Accounts account) throws SQLException {
        return accountRepository.save(account);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) throws SQLException {
        Accounts fromAccount = accountRepository.findById(request.getFromAccountId());
        checkOwnership(fromAccount);

        accountRepository.transferFunds(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount(),
                request.getDescription()
        );
        return "Transfer successful";
    }

    private void checkOwnership(Accounts account) {
        if (account == null) {
            throw new BankingException("Account not found.", 404);
        }
        if (account.getCustomerId() != authUtil.getCurrentCustomerId()) {
            throw new BankingException("You do not have access to this account.", 403);
        }
    }
}