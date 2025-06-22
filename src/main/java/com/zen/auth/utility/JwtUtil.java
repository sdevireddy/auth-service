package com.zen.auth.utility;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.zen.auth.config.JwtConfig;
import com.zen.auth.filters.ZenUserDetails;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private static final long ACCESS_TOKEN_VALIDITY_MS = 15 * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_VALIDITY_MS = 7L * 24 * 60 * 60 * 1000; // 7 days

    @Autowired
    private JwtConfig jwtConfig; // Provides secure SecretKey

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractTenant(String token) {
        return extractAllClaims(token).get("tenant", String.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getKey()) // ✅ use consistent key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String email, String tenantId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("tenant", tenantId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_MS))
                .signWith(jwtConfig.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String userName, String tenantId) {
        return Jwts.builder()
                .setSubject(userName)
                .claim("tenant", tenantId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_MS))
                .signWith(jwtConfig.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    

    public Boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // Will throw if token is invalid
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    public String extractTenantPrefix(String email) {
        String domain = email.substring(email.indexOf("@") + 1); // e.g. orgname.com
        return domain.split("\\.")[0]; // returns "orgname"
    }
    
    
}

