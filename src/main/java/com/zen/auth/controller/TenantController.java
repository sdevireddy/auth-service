package com.zen.auth.controller;

import com.zen.auth.dto.AuthRequest;
import com.zen.auth.dto.AuthResponse;
import com.zen.auth.dto.ApiResponse;
import com.zen.auth.dto.ZenTenantDTO;
import com.zen.auth.filters.TenantContextHolder;
import com.zen.auth.filters.ZenUserDetails;
import com.zen.auth.services.TenantService;
import com.zen.auth.services.ZenUserDetailsService;
import com.zen.auth.utility.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class TenantController {

    private static final Logger logger = LoggerFactory.getLogger(TenantController.class);

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ZenUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Create new tenant and admin user. Issue JWT tokens.
     */
    @PostMapping("/createAccount")
    public ResponseEntity<ApiResponse<AuthResponse>> createTenant(@RequestBody ZenTenantDTO dto) {
        logger.info("🎯 Creating tenant for org: {}", dto.getOrgName());

        try {
            tenantService.createTenant(dto);
            logger.info("✅ Tenant and admin created: {}", dto.getOrgName());

            String tenantId = TenantContextHolder.getTenantId();
            String accessToken = jwtUtil.generateToken(dto.getUserName(), tenantId);
            String refreshToken = jwtUtil.generateRefreshToken(dto.getUserName(), tenantId);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccess_token(accessToken);
            authResponse.setRefresh_token(refreshToken);
            authResponse.setOrgName(dto.getOrgName());
            authResponse.setUsername(dto.getUserName());
            authResponse.setTenantId(tenantId);

            logger.info("🔐 JWT issued for {}", dto.getUserName());

            return ResponseEntity.ok(new ApiResponse<>(true, "Tenant created successfully", authResponse));
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Tenant creation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("❌ Tenant creation error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating tenant: " + e.getMessage(), null));
        }
    }

    /**
     * Validate JWT token
     */
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Map<String, String>>> validateToken(HttpServletRequest request) {
        logger.info("🔍 Validating JWT token");

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("⚠️ Missing or invalid Authorization header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Missing or invalid token", null));
        }

        String jwt = authHeader.substring(7);
        if (!jwtUtil.validateToken(jwt)) {
            logger.warn("❌ Invalid JWT token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid token", null));
        }

        String email = jwtUtil.extractUsername(jwt);
        String tenantId = jwtUtil.extractTenant(jwt);
        TenantContextHolder.setTenantId(tenantId);

        logger.info("✅ Token valid for {}, tenantId: {}", email, tenantId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Token is valid", Map.of("email", email, "tenantId", tenantId)));
    }

    /**
     * Login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        logger.info("🔐 Login attempt for user: {}, email: {}", request.getUsername(), request.getEmail());

        try {
            String compoundUsername = request.getEmail() + "|" + request.getUsername();
            ZenUserDetails userDetails = (ZenUserDetails) userDetailsService.loadUserByUsername(compoundUsername);
            logger.debug("✅ User found for login: {}", request.getUsername());

            if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                logger.warn("⚠️ Invalid credentials for user: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid credentials", null));
            }

            if (userDetails.isFirstLogin()) {
                logger.info("🔒 First-time login for user: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "First-time login. Please reset your password.", null));
            }

            String tenantId = TenantContextHolder.getTenantId();
            String accessToken = jwtUtil.generateToken(request.getEmail(), tenantId);
            String refreshToken = jwtUtil.generateRefreshToken(request.getEmail(), tenantId);

            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshCookie);

            logger.info("✅ Login successful for user: {}", request.getUsername());

            return ResponseEntity.ok(new ApiResponse<>(true, "User Login Successful", Map.of(
                    "access_token", accessToken,
                    "refresh_token", refreshToken,
                    "tenantId", tenantId
            )));
        } catch (UsernameNotFoundException e) {
            logger.warn("❌ User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "User not found", null));
        } catch (Exception ex) {
            logger.error("🔥 Login error for user {}: {}", request.getUsername(), ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Login failed: " + ex.getMessage(), null));
        }
    }

    @GetMapping("/auth/ping")
    public ResponseEntity<String> ping() {
        logger.debug("🔄 Ping received");
        return ResponseEntity.ok("Auth service is alive");
    }
}
