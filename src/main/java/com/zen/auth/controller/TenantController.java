package com.zen.auth.controller;

import com.zen.auth.dto.AuthRequest;
import com.zen.auth.dto.AuthResponse;
import com.zen.auth.dto.ApiResponse;
import com.zen.auth.dto.ZenTenantDTO;
import com.zen.auth.filters.ZenUserDetails;
import com.zen.auth.services.TenantService;
import com.zen.auth.services.ZenUserDetailsService;
import com.zen.auth.utility.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class TenantController {

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
        try {
            // Create schema and admin user
            tenantService.createTenant(dto);

            String tenantId = jwtUtil.extractTenantPrefix(dto.getEmail());

            // Generate tokens
            String accessToken = jwtUtil.generateToken(dto.getEmail(), tenantId);
            String refreshToken = jwtUtil.generateRefreshToken(
            		dto.getEmail(), tenantId
            );

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccess_token(accessToken);
            authResponse.setRefresh_token(refreshToken);
            authResponse.setOrgName(dto.getOrgName());
            authResponse.setUsername(dto.getEmail());

            ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                    true,
                    "Tenant created successfully",
                    authResponse
            );

            return ResponseEntity.ok(apiResponse);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, e.getMessage(), null)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Error creating tenant: " + e.getMessage(), null)
            );
        }
    }

    /**
     * Login using tenant-specific user
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            String compoundUsername = email + "|" + username;
            ZenUserDetails userDetails = (ZenUserDetails) userDetailsService.loadUserByUsername(compoundUsername);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new ApiResponse<>(false, "Invalid credentials", null)
                );
            }

            if (userDetails.isFirstLogin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new ApiResponse<>(false, "First-time login. Please reset your password.", null)
                );
            }

            String tenantId = jwtUtil.extractTenantPrefix(email);
            String accessToken = jwtUtil.generateToken(username, tenantId);
            String refreshToken = jwtUtil.generateRefreshToken(username, tenantId);

            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshCookie);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccess_token(accessToken);
            authResponse.setRefresh_token(refreshToken);
            authResponse.setUsername(username);
            authResponse.setOrgName("Sample"); // replace with actual org
            authResponse.setRoles(userDetails.getRoleNames());
            authResponse.setModules(userDetails.getModules());

            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", authResponse));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>(false, "User not found", null)
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Login failed: " + ex.getMessage(), null)
            );
        }
    }
}
