package com.exist.HelpdeskApp.service.Implementations;

import com.exist.HelpdeskApp.exception.businessexceptions.InvalidCredentialsException;
import com.exist.HelpdeskApp.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key signingKey;
    private final long EXPIRATION_TIME = 100L * 60 * 60 * 1000; // 1 HOUR

    @PostConstruct
    public void init() {
        try {
            if (secretKey == null || secretKey.isEmpty()) { // If secretKey not set, generate a temporary key for dev/test
                KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
                SecretKey key = keyGen.generateKey();
                secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
                System.out.println("Generated temporary secretKey (dev only): " + secretKey);
            }

            byte[] keyBytes = Base64.getDecoder().decode(secretKey);
            signingKey = Keys.hmacShaKeyFor(keyBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to initialize secret key", e);
        }
    }

    @Override
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

        } catch (SignatureException e) {
            throw new InvalidCredentialsException("Invalid token signature!");
        } catch (ExpiredJwtException e) {
            throw new InvalidCredentialsException("Token expired");
        } catch (MalformedJwtException e) {
            throw new InvalidCredentialsException("Malformed token");
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

//    private Key getSigningKey() {
//        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

}
