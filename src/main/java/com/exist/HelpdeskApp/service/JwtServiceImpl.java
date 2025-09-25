package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.service.Implementations.JwtService;

public class JwtServiceImpl implements JwtService {
    private String secretKey;
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 HOUR

    @Override
    public void init() {

    }

    @Override
    public String generateToken(String Username) {
        return "";
    }

    @Override
    public String extractUsername(String token) {
        return "";
    }

    @Override
    public boolean isTokenValid(String token) {
        return false;
    }

    @Override
    public boolean isTokenExpired(String token) {
        return false;
    }
}
