package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.account.AccountRequest;
import com.exist.HelpdeskApp.exception.businessexceptions.InvalidCredentialsException;
import com.exist.HelpdeskApp.service.Implementations.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
    private JwtServiceImpl jwtService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public HomeController(JwtServiceImpl jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping("/about")
    public String about() {
        return "This is an ECC Spring Boot project made by Kendrick Chua";
    }

    @RequestMapping("/")
    public String home() {
        return "Welcome to the Helpdesk Application!";
    }

    @PostMapping("/login")
    public String login(@RequestBody AccountRequest accountRequest){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(accountRequest.getUsername(), accountRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Your username or password is incorrect!");
        }
        return jwtService.generateToken(accountRequest.getUsername());
    }
}
