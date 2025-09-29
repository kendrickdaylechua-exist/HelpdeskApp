package com.exist.HelpdeskApp.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String generateToken(String Username);
    public String extractUsername(String token);
    public boolean validateToken(String token, UserDetails userDetails);
    public boolean isTokenExpired(String token);
}
