package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.account.AccountListRequest;
import com.exist.HelpdeskApp.dto.account.AccountRequest;
import com.exist.HelpdeskApp.dto.account.AccountResponse;
import com.exist.HelpdeskApp.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountServiceImpl accountService;

    @Autowired
    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AccountResponse> getAccounts(@ModelAttribute AccountListRequest request) {
        return accountService.getAccounts(request);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse createAccount(@RequestBody @Valid AccountRequest request) {
        return accountService.createAccount(request);
    }

    @PatchMapping("/{username}")
    public AccountResponse updateAccount(@PathVariable String username,
                                         @RequestBody AccountRequest request,
                                         Authentication authentication,
                                         @RequestParam(defaultValue = "false") boolean dissociate) {
        return accountService.updateAccount(username, request, authentication, dissociate);
    }

    @GetMapping("{username}")
    public AccountResponse getAccount(@PathVariable String username) {
        return accountService.getAccountByUsername(username);
    }

    @PatchMapping("/{username}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountResponse> updateStatus(
            @PathVariable String username,
            @RequestParam boolean enabled,
            Authentication authentication
    ) {
        return ResponseEntity.ok(accountService.updateStatus(username, authentication, enabled));
    }
}
