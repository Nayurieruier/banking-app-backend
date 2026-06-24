package com.example.demo.Controller;

import com.example.demo.Config.AuthUtil;
import com.example.demo.Exception.BankingException;
import com.example.demo.Model.Accounts;
import com.example.demo.Model.Transactions;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;

    public TransactionController(TransactionRepository transactionRepository,
                                 AccountRepository accountRepository,
                                 AuthUtil authUtil) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.authUtil = authUtil;
    }

    @GetMapping("/{transactionId}")
    public Transactions getTransaction(@PathVariable long transactionId) throws SQLException {
        return transactionRepository.findById(transactionId);
    }

    @GetMapping("/account/{accountId}")
    public List<Transactions> getTransactionsForAccount(@PathVariable long accountId) throws SQLException {
        checkAccountOwnership(accountId);
        return transactionRepository.findByAccountId(accountId);
    }

    @GetMapping("/account/{accountId}/recent")
    public List<Transactions> getRecentTransactions(
            @PathVariable long accountId,
            @RequestParam(defaultValue = "10") int limit) throws SQLException {
        checkAccountOwnership(accountId);
        return transactionRepository.findRecentByAccountId(accountId, limit);
    }

    private void checkAccountOwnership(long accountId) throws SQLException {
        Accounts account = accountRepository.findById(accountId);
        if (account == null) {
            throw new BankingException("Account not found.", 404);
        }
        if (account.getCustomerId() != authUtil.getCurrentCustomerId()) {
            throw new BankingException("You do not have access to this account's transactions.", 403);
        }
    }
}