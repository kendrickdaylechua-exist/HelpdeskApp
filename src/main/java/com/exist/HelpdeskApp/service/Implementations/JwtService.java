package com.exist.HelpdeskApp.service.Implementations;

import java.security.Key;

public interface JwtService {
    public void init();
    public String generateToken(String Username);
    public String extractUsername(String token);
    public boolean isTokenValid(String token);
    public boolean isTokenExpired(String token);
}
