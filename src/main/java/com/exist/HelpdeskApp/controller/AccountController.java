package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.account.AccountListRequest;
import com.exist.HelpdeskApp.dto.account.AccountRequest;
import com.exist.HelpdeskApp.dto.account.AccountResponse;
import com.exist.HelpdeskApp.service.Implementations.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountServiceImpl accountService;

    @Autowired
    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Page<AccountResponse> getAccounts(@ModelAttribute AccountListRequest request) {
        return accountService.getAccounts(request);
    }

    @PostMapping
    public AccountResponse createAccount(@ModelAttribute @Valid AccountRequest request) {
        return accountService.createAccount(request);
    }

    @PatchMapping("/{username}")
    public AccountResponse updateAccount(@PathVariable String username, @ModelAttribute AccountRequest request) {
        return accountService.updateAccount(username, request);
    }

    @PatchMapping("/{username}/status")
    public ResponseEntity<AccountResponse> updateStatus(
            @PathVariable String username,
            @RequestBody Map<String, Boolean> body
    ) {
        boolean enabled = body.get("enabled");
        return ResponseEntity.ok(accountService.updateStatus(username, enabled));
    }

}
