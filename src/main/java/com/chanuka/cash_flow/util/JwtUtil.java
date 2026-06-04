package com.chanuka.cash_flow.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

     // Creates the signing key from application.properties secret
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

     // Generate JWT token using email
    public String generateToken(String email) {

        // expiry time
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        // build JWT
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

     // Extract email from JWT token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

     // Extract expiration date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

     // Generic method to extract claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

     // Extract all JWT claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

     // Check if token is expired
    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

     // Validate token against email
    public boolean validateToken(String token, UserDetails userDetails) {

        String tokenEmail = extractEmail(token);

        return tokenEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}