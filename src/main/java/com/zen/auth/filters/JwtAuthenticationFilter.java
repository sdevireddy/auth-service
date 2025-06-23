package com.zen.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zen.auth.dto.ApiResponse;
import com.zen.auth.services.ZenUserDetailsService;
import com.zen.auth.utility.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ZenUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ⛔ Skip these endpoints from JWT validation
        if (path.equals("/auth/login") || path.equals("/auth/createAccount") || path.equals("/auth/validate")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String jwt = authHeader.substring(7);

        try {
            String email = jwtUtil.extractUsername(jwt);
            String tenantId = jwtUtil.extractTenant(jwt);

            if (email != null && tenantId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String compoundUsername = tenantId + "|" + email;
                var userDetails = userDetailsService.loadUserByUsername(compoundUsername);

                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                    return;
                }
            }

        } catch (Exception ex) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT validation failed: " + ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        ApiResponse<String> error = new ApiResponse<>(false, message, null);
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), error);
    }
}
