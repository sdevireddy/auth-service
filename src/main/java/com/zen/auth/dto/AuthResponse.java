package com.zen.auth.dto;

import java.util.List;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AuthResponse {

    private String username;
    private List<String> roles;
    private String token_type = "Bearer";
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private List<String> modules;
    private Long orgId;
    private String orgName;
    private String tenantId;

    // ✅ Additional fields for full permission details
    private Set<String> permissions;
    private Map<String, Map<String, Set<String>>> moduleFeaturePermissions;
    private Set<String> permissionBundles;
    private List<Map<String, Object>> fieldLevelPermissions;
    private List<Map<String, Object>> recordLevelPermissions;
    private Set<String> branches;
    private Set<String> locations;

    // === Getters and Setters ===

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Map<String, Map<String, Set<String>>> getModuleFeaturePermissions() {
        return moduleFeaturePermissions;
    }

    public void setModuleFeaturePermissions(Map<String, Map<String, Set<String>>> moduleFeaturePermissions) {
        this.moduleFeaturePermissions = moduleFeaturePermissions;
    }

    public Set<String> getPermissionBundles() {
        return permissionBundles;
    }

    public void setPermissionBundles(Set<String> permissionBundles) {
        this.permissionBundles = permissionBundles;
    }

    public List<Map<String, Object>> getFieldLevelPermissions() {
        return fieldLevelPermissions;
    }

    public void setFieldLevelPermissions(List<Map<String, Object>> fieldLevelPermissions) {
        this.fieldLevelPermissions = fieldLevelPermissions;
    }

    public List<Map<String, Object>> getRecordLevelPermissions() {
        return recordLevelPermissions;
    }

    public void setRecordLevelPermissions(List<Map<String, Object>> recordLevelPermissions) {
        this.recordLevelPermissions = recordLevelPermissions;
    }

    public Set<String> getBranches() {
        return branches;
    }

    public void setBranches(Set<String> branches) {
        this.branches = branches;
    }

    public Set<String> getLocations() {
        return locations;
    }

    public void setLocations(Set<String> locations) {
        this.locations = locations;
    }

    // === Constructors ===

    public AuthResponse() {
        super();
    }

    public AuthResponse(String token, String username, String tenantId) {
        this.access_token = token;
        this.username = username;
        this.tenantId = tenantId;
    }
}
