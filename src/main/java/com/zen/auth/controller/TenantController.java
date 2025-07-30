package com.zen.auth.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.auth.common.entity.PermissionBundle;
import com.zen.auth.common.entity.Permissions;
import com.zen.auth.common.entity.Roles;
import com.zen.auth.common.entity.ZenUser;
import com.zen.auth.dto.ApiResponse;
import com.zen.auth.dto.AuthRequest;
import com.zen.auth.dto.AuthResponse;
import com.zen.auth.dto.ZenTenantDTO;
import com.zen.auth.entitymanagers.DynamicTenantManager;
import com.zen.auth.filters.TenantContextHolder;
import com.zen.auth.filters.ZenUserDetails;
import com.zen.auth.repository.FeatureRepository;
import com.zen.auth.repository.ModuleRepository;
import com.zen.auth.repository.PermissionRepository;
import com.zen.auth.repository.UsersRepository;
import com.zen.auth.services.RoleProvisioningService;
import com.zen.auth.services.TenantService;
import com.zen.auth.services.ZenUserDetailsService;
import com.zen.auth.utility.JwtUtil;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class TenantController {

    private static final Logger logger = LoggerFactory.getLogger(TenantController.class);

    @Autowired
    private TenantService tenantService;

    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired private ModuleRepository moduleRepository;

    @Autowired
    private ZenUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DynamicTenantManager tenantManager;

    @Autowired
	private FeatureRepository featureRepository;

    @Autowired
	private PermissionRepository permissionRepository;
    
    @Autowired
    private RoleProvisioningService roleProvisioningService;

	/*
	 * @PostMapping("/createAccount") public
	 * ResponseEntity<ApiResponse<AuthResponse>> createTenant(@RequestBody
	 * ZenTenantDTO dto) { logger.info("🎯 Creating tenant for org: {}",
	 * dto.getOrgName()); try { tenantService.createTenant(dto);
	 * logger.info("✅ Tenant and admin created: {}", dto.getOrgName());
	 * 
	 * String tenantId = TenantContextHolder.getTenantId(); String accessToken =
	 * jwtUtil.generateToken(dto.getUserName(), tenantId); String refreshToken =
	 * jwtUtil.generateRefreshToken(dto.getUserName(), tenantId);
	 * 
	 * AuthResponse authResponse = new AuthResponse();
	 * authResponse.setAccess_token(accessToken);
	 * authResponse.setRefresh_token(refreshToken);
	 * authResponse.setOrgName(dto.getOrgName());
	 * authResponse.setUsername(dto.getUserName());
	 * authResponse.setTenantId(tenantId);
	 * 
	 * logger.info("🔐 JWT issued for {}", dto.getUserName());
	 * 
	 * return ResponseEntity.ok(new ApiResponse<>(true,
	 * "Tenant created successfully", authResponse)); } catch
	 * (IllegalArgumentException e) { logger.warn("⚠️ Tenant creation failed: {}",
	 * e.getMessage()); return ResponseEntity.badRequest().body(new
	 * ApiResponse<>(false, e.getMessage(), null)); } catch (Exception e) {
	 * logger.error("❌ Tenant creation error: {}", e.getMessage(), e); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(new
	 * ApiResponse<>(false, "Error creating tenant: " + e.getMessage(), null)); } }
	 */

	/*
	 * @PostMapping("/createAccount") public
	 * ResponseEntity<ApiResponse<AuthResponse>> createTenant(@RequestBody
	 * ZenTenantDTO dto) { logger.info("🎯 Creating tenant for org: {}",
	 * dto.getOrgName());
	 * 
	 * try { tenantService.createTenant(dto); // Creates schema, tables, inserts
	 * user, roles etc. logger.info("✅ Tenant and admin created: {}",
	 * dto.getOrgName());
	 * 
	 * logger.info("Create Modules for Tenant");
	 * 
	 * String tenantId = TenantContextHolder.getTenantId(); String accessToken =
	 * jwtUtil.generateToken(dto.getUserName(), tenantId); String refreshToken =
	 * jwtUtil.generateRefreshToken(dto.getUserName(), tenantId);
	 * 
	 * // Fetch user and permissions after account creation
	 * logger.info("✅ Get roles fro the user : {}", dto.getUserName());
	 * EntityManager em = tenantManager.getEntityManagerForTenant(tenantId); ZenUser
	 * user = em.createQuery("SELECT u FROM ZenUser u WHERE u.email = :username",
	 * ZenUser.class) .setParameter("username", dto.getUserName())
	 * .getSingleResult();
	 * 
	 * logger.info("✅ Get roles Roles  : {}", dto.getUserName()); List<String>
	 * roleNames = user.getRoles().stream() .map(Roles::getName) .toList();
	 * 
	 * Set<String> flatPermissions = user.getRoles().stream() .flatMap(role ->
	 * role.getPermissions().stream()) .map(Permissions::getName)
	 * .collect(Collectors.toSet());
	 * 
	 * Set<String> permissionBundles = user.getRoles().stream() .flatMap(role ->
	 * role.getPermissionBundles().stream()) .map(PermissionBundle::getName)
	 * .collect(Collectors.toSet());
	 * 
	 * Map<String, Map<String, Set<String>>> moduleFeaturePermissions = new
	 * HashMap<>(); user.getRoles().forEach(role -> {
	 * role.getFeaturePermissions().forEach(rfp -> { String moduleName =
	 * rfp.getFeature().getModule().getName(); String featureName =
	 * rfp.getFeature().getName(); String permissionName =
	 * rfp.getPermission().getName();
	 * 
	 * moduleFeaturePermissions .computeIfAbsent(moduleName, k -> new HashMap<>())
	 * .computeIfAbsent(featureName, k -> new java.util.HashSet<>())
	 * .add(permissionName); }); });
	 * 
	 * Set<String> bundles = user.getRoles().stream() .flatMap(role ->
	 * role.getPermissionBundles().stream()) .map(PermissionBundle::getName)
	 * .collect(Collectors.toSet());
	 * 
	 * List<Map<String, Object>> fieldPermissions = user.getRoles().stream()
	 * .flatMap((Roles role) -> { Stream<Map<String, Object>> mapStream =
	 * role.getFieldPermissions().stream().map(fp -> { Map<String, Object> map = new
	 * HashMap<>(); map.put("role", role.getName()); map.put("module",
	 * fp.getModule().getName()); map.put("feature", fp.getFeature().getName());
	 * map.put("field", fp.getFieldName()); map.put("action", fp.getAction());
	 * return map; }); return mapStream; }) .collect(Collectors.toList());
	 * 
	 * Set<RecordPermission> recordPermissionSet =
	 * Optional.ofNullable(user.getRecordPermissions())
	 * .orElse(Collections.emptySet());
	 * 
	 * List<Map<String, Object>> recordPermissions =
	 * Optional.ofNullable(user.getRecordPermissions()) .orElse(Set.of()) .stream()
	 * .map(rp -> { Map<String, Object> map = new HashMap<>(); map.put("recordType",
	 * rp.getRecordType()); map.put("recordId", rp.getRecordId());
	 * map.put("permissionType", rp.getPermissionType()); return map; })
	 * .collect(Collectors.toList()); // Use toList() if Java 17 is correctly
	 * configured
	 * 
	 * Set<String> branchNames = user.getBranches().stream() .map(b ->
	 * b.getName()).collect(Collectors.toSet());
	 * 
	 * Set<String> locationNames = user.getLocations().stream() .map(l ->
	 * l.getName()).collect(Collectors.toSet());
	 * 
	 * // Build Response DTO AuthResponse authResponse = new AuthResponse();
	 * authResponse.setAccess_token(accessToken);
	 * authResponse.setRefresh_token(refreshToken);
	 * authResponse.setOrgName(dto.getOrgName());
	 * authResponse.setUsername(dto.getUserName());
	 * authResponse.setTenantId(tenantId); authResponse.setRoles(roleNames);
	 * authResponse.setPermissions(flatPermissions);
	 * authResponse.setModuleFeaturePermissions(moduleFeaturePermissions);
	 * authResponse.setPermissionBundles(permissionBundles);
	 * authResponse.setFieldLevelPermissions(fieldPermissions);
	 * authResponse.setRecordLevelPermissions(recordPermissions);
	 * authResponse.setBranches(branchNames);
	 * authResponse.setLocations(locationNames);
	 * 
	 * logger.info("🔐 Full access info returned for new tenant {}",
	 * dto.getOrgName());
	 * 
	 * return ResponseEntity.ok(new ApiResponse<>(true,
	 * "Tenant created successfully", authResponse)); } catch
	 * (IllegalArgumentException e) { logger.warn("⚠️ Tenant creation failed: {}",
	 * e.getMessage()); return ResponseEntity.badRequest().body(new
	 * ApiResponse<>(false, e.getMessage(), null)); } catch (Exception e) {
	 * logger.error("❌ Tenant creation error: {}", e.getMessage(), e); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(new
	 * ApiResponse<>(false, "Error creating tenant: " + e.getMessage(), null)); } }
	 * 
	 * @PostMapping("/validate")
	 */
	
    @PostMapping("/createAccount")
    public ResponseEntity<ApiResponse<AuthResponse>> createTenant(@RequestBody ZenTenantDTO dto) {
        logger.info("\uD83C\uDFC3 Creating tenant for org: {}", dto.getOrgName());

        try {
            tenantService.createTenant(dto);
            String tenantId = TenantContextHolder.getTenantId();
            EntityManager em = tenantManager.getEntityManagerForTenant(tenantId);

            ZenUser adminUser = em.createQuery("SELECT u FROM ZenUser u WHERE u.email = :username", ZenUser.class)
                    .setParameter("username", dto.getUserName())
                    .getSingleResult();

            for (String moduleKey : dto.getModules()) {
            		String roleName = moduleKey.toUpperCase() + "_Admin"; // e.g., "crm" → "CRM_Admin"
            		Set<String> assignRoleNames = Set.of(roleName);
            		roleProvisioningService.createTenantRoles(tenantId, moduleKey, adminUser, assignRoleNames);
            }

            em.merge(adminUser);

            String accessToken = jwtUtil.generateToken(dto.getUserName(), tenantId);
            String refreshToken = jwtUtil.generateRefreshToken(dto.getUserName(), tenantId);

            List<String> roleNames = adminUser.getRoles().stream().map(Roles::getName).toList();
            Set<String> flatPermissions = adminUser.getRoles().stream()
                    .flatMap(r -> r.getPermissions().stream())
                    .map(Permissions::getName).collect(Collectors.toSet());

            Set<String> permissionBundles = adminUser.getRoles().stream()
                    .flatMap(r -> r.getPermissionBundles().stream())
                    .map(PermissionBundle::getName).collect(Collectors.toSet());

            Map<String, Map<String, Set<String>>> moduleFeaturePermissions = new HashMap<>();
            adminUser.getRoles().forEach(role -> {
                role.getFeaturePermissions().forEach(rfp -> {
                    String moduleName = rfp.getFeature().getModule().getName();
                    String featureName = rfp.getFeature().getName();
                    String permissionName = rfp.getPermission().getName();
                    moduleFeaturePermissions.computeIfAbsent(moduleName, k -> new HashMap<>())
                            .computeIfAbsent(featureName, k -> new HashSet<>()).add(permissionName);
                });
            });

            List<Map<String, Object>> fieldPermissions = adminUser.getRoles().stream()
                    .flatMap(role -> role.getFieldPermissions().stream().map(fp -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("role", role.getName());
                        map.put("module", fp.getModule().getName());
                        map.put("feature", fp.getFeature().getName());
                        map.put("field", fp.getFieldName());
                        map.put("action", fp.getAction());
                        return map;
                    })).collect(Collectors.toList());

            List<Map<String, Object>> recordPermissions = Optional.ofNullable(adminUser.getRecordPermissions())
                    .orElse(Set.of())
                    .stream().map(rp -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("recordType", rp.getRecordType());
                        map.put("recordId", rp.getRecordId());
                        map.put("permissionType", rp.getPermissionType());
                        return map;
                    }).collect(Collectors.toList());

            Set<String> branchNames = adminUser.getBranches().stream().map(b -> b.getName()).collect(Collectors.toSet());
            Set<String> locationNames = adminUser.getLocations().stream().map(l -> l.getName()).collect(Collectors.toSet());

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccess_token(accessToken);
            authResponse.setRefresh_token(refreshToken);
            authResponse.setOrgName(dto.getOrgName());
            authResponse.setUsername(dto.getUserName());
            authResponse.setTenantId(tenantId);
            authResponse.setRoles(roleNames);
            authResponse.setPermissions(flatPermissions);
            authResponse.setModuleFeaturePermissions(moduleFeaturePermissions);
            authResponse.setPermissionBundles(permissionBundles);
            authResponse.setFieldLevelPermissions(fieldPermissions);
            authResponse.setRecordLevelPermissions(recordPermissions);
            authResponse.setBranches(branchNames);
            authResponse.setLocations(locationNames);

            return ResponseEntity.ok(new ApiResponse<>(true, "Tenant created successfully", authResponse));

        } catch (IllegalArgumentException e) {
            logger.warn("\u26A0\uFE0F Tenant creation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("\u274C Tenant creation error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating tenant: " + e.getMessage(), null));
        }
    }

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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        logger.info("🔐 Login attempt for user: {}, email: {}", request.getUsername(), request.getEmail());

        try {
            String compoundUsername = request.getEmail() + "|" + request.getUsername();
            ZenUserDetails userDetails = (ZenUserDetails) userDetailsService.loadUserByUsername(compoundUsername);

            if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid credentials", null));
            }

            if (userDetails.isFirstLogin()) {
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

            EntityManager em = tenantManager.getEntityManagerForTenant(tenantId);
            ZenUser user = em.find(ZenUser.class, userDetails.getId());

            List<String> roleNames = user.getRoles().stream().map(Roles::getName).toList();

            Set<String> flatPermissions = user.getRoles().stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(Permissions::getName)
                    .collect(Collectors.toSet());

            Set<String> permissionBundles = user.getRoles().stream()
                    .flatMap(role -> role.getPermissionBundles().stream())
                    .map(PermissionBundle::getName)
                    .collect(Collectors.toSet());

            Map<String, Map<String, Set<String>>> moduleFeaturePermissions = new HashMap<>();
            user.getRoles().forEach(role -> {
                role.getFeaturePermissions().forEach(rfp -> {
                    String moduleName = rfp.getFeature().getModule().getName();
                    String featureName = rfp.getFeature().getName();
                    String permissionName = rfp.getPermission().getName();

                    moduleFeaturePermissions
                            .computeIfAbsent(moduleName, k -> new HashMap<>())
                            .computeIfAbsent(featureName, k -> new java.util.HashSet<>())
                            .add(permissionName);
                });
            });

            List<Map<String, Object>> fieldPermissions = user.getRoles().stream()
                    .flatMap(role -> role.getFieldPermissions().stream().map(fp -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("role", role.getName());
                        map.put("module", fp.getModule().getName());
                        map.put("feature", fp.getFeature().getName());
                        map.put("field", fp.getFieldName());
                        map.put("action", fp.getAction());
                        return map;
                    }))
                    .collect(Collectors.toList());

            List<Map<String, Object>> recordPermissions = Optional.ofNullable(user.getRecordPermissions())
                    .orElse(Collections.emptySet())
                    .stream()
                    .map(rp -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("recordType", rp.getRecordType());
                        map.put("recordId", rp.getRecordId());
                        map.put("permissionType", rp.getPermissionType());
                        return map;
                    })
                    .collect(Collectors.toList());

            Set<String> branchNames = user.getBranches().stream()
                    .map(b -> b.getName()).collect(Collectors.toSet());

            Set<String> locationNames = user.getLocations().stream()
                    .map(l -> l.getName()).collect(Collectors.toSet());

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccess_token(accessToken);
            authResponse.setRefresh_token(refreshToken);
            authResponse.setOrgName(user.getUsername()); // or set proper orgName if available
            authResponse.setUsername(user.getUsername());
            authResponse.setTenantId(tenantId);
            authResponse.setRoles(roleNames);
            authResponse.setPermissions(flatPermissions);
            authResponse.setModuleFeaturePermissions(moduleFeaturePermissions);
            authResponse.setPermissionBundles(permissionBundles);
            authResponse.setFieldLevelPermissions(fieldPermissions);
            authResponse.setRecordLevelPermissions(recordPermissions);
            authResponse.setBranches(branchNames);
            authResponse.setLocations(locationNames);

            logger.info("✅ Login success - full permission graph returned");
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", authResponse));

        } catch (UsernameNotFoundException e) {
            logger.warn("❌ User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "User not found", null));
        } catch (Exception e) {
            logger.error("🔥 Login error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Login failed: " + e.getMessage(), null));
        }
    }

 


    @GetMapping("/auth/ping")
    public ResponseEntity<String> ping() {
        logger.debug("🔄 Ping received");
        return ResponseEntity.ok("Auth service is alive");
    }
}
