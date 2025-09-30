package com.exist.HelpdeskApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping("/about")
    public String about() {
        return "This is an ECC Spring Boot project made by Kendrick Chua";
    }

    @RequestMapping
    public String home() {
        return "Welcome to the Helpdesk Application!";
    }
}
