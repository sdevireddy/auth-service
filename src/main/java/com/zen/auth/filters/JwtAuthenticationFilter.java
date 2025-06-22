package com.zen.auth.filters;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zen.auth.dto.ApiResponse;
import com.zen.auth.services.ZenUserDetailsService;
import com.zen.auth.utility.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ZenUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
    	
    	try {
    		final String authorizationHeader = request.getHeader("Authorization");

            String jwt = null;
            String email = null;
            String tenantId = null;
           // TenantContextHolder.setTenantId(request.getTenantId());
            
            String requestPath = request.getRequestURI();
            
            
            
            if (requestPath.equals("/auth/login") || requestPath.equals("/auth/createAccount")) {
            	 CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
            	    
            	    // Read JSON body
            	    String body = new String(wrappedRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            	    ObjectMapper mapper = new ObjectMapper();
            	    JsonNode jsonNode = mapper.readTree(body);
            	    
            	    
            	    //JsonNode tenantNode = jsonNode.get("orgId");
            	    JsonNode emailNode = jsonNode.get("email");

            	    if (emailNode == null || emailNode.isNull()) {
            	        writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Missing email in request");
            	        return;
            	    }
            	    
            	    String orgName = jwtUtil.extractTenantPrefix(emailNode.asText());
            	    
            	    // String StenantId = jsonNode.get("orgId").asText(); // ✅ Extract tenantId
            	   // TenantContextHolder.setTenantId(Long.valueOf(StenantId)); // ✅ Set tenant for this request
            	    TenantContextHolder.setTenantId(orgName);

            	    filterChain.doFilter(wrappedRequest, response); // Continue with wrapped request
            	    return;
            }

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                try {
                    email = jwtUtil.extractUsername(jwt); // Extracts subject from JWT
                    tenantId = jwtUtil.extractTenant(jwt);   // Custom claim "tenant"
                } catch (Exception e) {
                	e.printStackTrace();
                	    writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                	    return;
                }
            }

            if (email != null && tenantId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Combine tenant and username to pass into UserDetailsService
                String compoundUsername = tenantId + "|" + email;
                System.out.println("compoundUsername******" + compoundUsername);
                ZenUserDetails userDetails = (ZenUserDetails) userDetailsService.loadUserByUsername(compoundUsername);

                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {

                    // First login protection
                    if (userDetails.isFirstLogin() && !request.getRequestURI().contains("/reset-password")) {
                    	  writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "First-time login: password reset required");
                        return;
                    }

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
    	}catch(Exception aEx) {
    		aEx.printStackTrace();
    	}
        filterChain.doFilter(request, response);
    }
    
    public String extractTenantFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return email.substring(email.indexOf("@") + 1); // returns "org.com"
    }
    
    
    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<String> errorResponse = new ApiResponse<>(false, message, null);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);
    }
    
  

}
