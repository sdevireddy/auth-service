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
     * Tenant creation + admin user + JWT tokens
     */
    @PostMapping("/createAccount")
    public ResponseEntity<ApiResponse<AuthResponse>> createTenant(@RequestBody ZenTenantDTO dto) {
        logger.info("🎯 Received request to create tenant for org: {}", dto.getOrgName());

        try {
            tenantService.createTenant(dto);
            logger.info("✅ Tenant and admin user created for org: {}", dto.getOrgName());

            String tenantId = jwtUtil.extractTenantPrefix(dto.getUserName());
            logger.debug("Extracted tenantId: {}", tenantId);

            String accessToken = jwtUtil.generateToken(dto.getUserName(), TenantContextHolder.getTenantId());
            String refreshToken = jwtUtil.generateRefreshToken(dto.getUserName(), TenantContextHolder.getTenantId());

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccess_token(accessToken);
            authResponse.setRefresh_token(refreshToken);
            authResponse.setOrgName(dto.getOrgName());
            authResponse.setUsername(dto.getUserName());
            authResponse.setTenantId(TenantContextHolder.getTenantId());

            logger.info("🔐 JWT tokens generated for user: {}", dto.getUserName());

            return ResponseEntity.ok(new ApiResponse<>(true, "Tenant created successfully", authResponse));

        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Validation error while creating tenant: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("❌ Error creating tenant: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating tenant: " + e.getMessage(), null));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Map<String, String>>> validateToken(HttpServletRequest request) {
        logger.info("🔍 Validating JWT token");

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("⚠️ Missing or invalid Authorization header");
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Missing or invalid token", null));
        }

        String jwt = authHeader.substring(7);
        if (!jwtUtil.validateToken(jwt)) {
            logger.warn("❌ Invalid token received");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ApiResponse<>(false, "Invalid token", null));
        }

        String email = jwtUtil.extractUsername(jwt);
        String tenantId = jwtUtil.extractTenant(jwt);
        TenantContextHolder.setTenantId(tenantId);

        logger.info("✅ Token validated for email: {}, tenantId: {}", email, tenantId);

        return ResponseEntity.ok(new ApiResponse<>(true, "Token is valid", Map.of("email", email, "tenantId", tenantId)));
    }

    /**
     * Login using tenant-specific user
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        logger.info("🔐 Login attempt for user: {} under email: {}", request.getUsername(), request.getEmail());

        String email = request.getEmail();
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            String compoundUsername = email + "|" + username;
            ZenUserDetails userDetails = (ZenUserDetails) userDetailsService.loadUserByUsername(compoundUsername);
            logger.debug("User loaded successfully for login");

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                logger.warn("⚠️ Invalid credentials for user: {}", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new ApiResponse<>(false, "Invalid credentials", null));
            }

            if (userDetails.isFirstLogin()) {
                logger.info("🔒 First login detected for user: {}", username);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new ApiResponse<>(false, "First-time login. Please reset your password.", null));
            }

            String tenantId = jwtUtil.extractTenantPrefix(email);
            String accessToken = jwtUtil.generateToken(email, TenantContextHolder.getTenantId());
            String refreshToken = jwtUtil.generateRefreshToken(email, TenantContextHolder.getTenantId());

            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            response.addCookie(refreshCookie);

            logger.info("✅ Login successful for user: {}", username);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "User Login Successfull", Map.of(
                            "access_token", accessToken,
                            "refresh_token", refreshToken,
                            "tenantId", TenantContextHolder.getTenantId()
                    ))
            );
        } catch (UsernameNotFoundException e) {
            logger.warn("❌ Username not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>(false, "User not found", null));
        } catch (Exception ex) {
            logger.error("🔥 Login failed for user {}: {}", username, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Login failed: " + ex.getMessage(), null));
        }
    }

    @GetMapping("/auth/ping")
    public ResponseEntity<String> ping() {
        logger.debug("🔄 Ping received");
        return ResponseEntity.ok("Auth service is alive");
    }
}
