package com.zen.auth.utility;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.zen.auth.config.JwtConfig;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final long ACCESS_TOKEN_VALIDITY_MS = 15 * 60 * 1000; // 15 minutes
    private static final long REFRESH_TOKEN_VALIDITY_MS = 7L * 24 * 60 * 60 * 1000; // 7 days

    @Autowired
    private JwtConfig jwtConfig;

    // Extract claims
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractTenant(String token) {
        return extractAllClaims(token).get("tenant", String.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Token Generators
    public String generateToken(String email, String tenantId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("tenant", tenantId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_MS))
                .signWith(jwtConfig.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email, String tenantId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("tenant", tenantId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_MS))
                .signWith(jwtConfig.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token); // Will throw if token is invalid
            System.out.println("✅ Claims: " + claims);
            boolean expired = isTokenExpired(token);
            System.out.println("🕒 Expired? " + expired);
            return !expired;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("❌ JWT Exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }
    public boolean validateToken(String token, String expectedUsername) {
        try {
            String actualUsername = extractUsername(token);
            return actualUsername.equals(expectedUsername) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Optional: extract org prefix from email
    public String extractTenantPrefix(String email) {
        String domain = email.substring(email.indexOf("@") + 1);
        return domain.split("\\.")[0]; // e.g. zen from zen.com
    }
}
