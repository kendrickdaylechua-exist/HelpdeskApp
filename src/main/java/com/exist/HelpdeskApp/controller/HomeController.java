package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.account.AccountRequest;
import com.exist.HelpdeskApp.dto.account.AccountResponse;
import com.exist.HelpdeskApp.service.impl.AccountServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    private final AccountServiceImpl accountService;

    public HomeController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @RequestMapping("/about")
    public String about() {
        return "This is an ECC Spring Boot project made by Kendrick Chua";
    }

    @RequestMapping
    public String home() {
        return "Welcome to the Helpdesk Application!";
    }

    @PostMapping("/login")
    public String login(@RequestBody AccountRequest accountRequest){
        return accountService.accountLogin(accountRequest);
    }

    @GetMapping("/me")
    public AccountResponse getOwnAccount(Authentication authentication) {
        return accountService.getAccountByUsername(authentication.getName());
    }

    @PatchMapping("/me")
    public AccountResponse updateAccount(@RequestBody AccountRequest request,
                                         Authentication authentication,
                                         @RequestParam(defaultValue = "false") boolean dissociate) {
        return accountService.updateAccount(authentication.getName(), request, authentication, dissociate);
    }
}
