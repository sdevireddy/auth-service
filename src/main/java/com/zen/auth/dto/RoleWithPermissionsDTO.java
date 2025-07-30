package com.zen.auth.dto;


import java.util.Map;
import java.util.Set;

public class RoleWithPermissionsDTO {
	private String roleName;
	private boolean isDefault;
	private String moduleKey;
	private Map<String, Set<String>> featurePermissions;

	// Constructors
	public RoleWithPermissionsDTO() {
	}

    // ✅ FIX: Add moduleKey to the constructor
    public RoleWithPermissionsDTO(String roleName, boolean isDefault, String moduleKey,
                                  Map<String, Set<String>> featurePermissions) {
        this.roleName = roleName;
        this.isDefault = isDefault;
        this.moduleKey = moduleKey;
        this.featurePermissions = featurePermissions;
    }
	// Getters and Setters
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getModuleKey() {
		return moduleKey;
	}

	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}

	public Map<String, Set<String>> getFeaturePermissions() {
		return featurePermissions;
	}

	public void setFeaturePermissions(Map<String, Set<String>> featurePermissions) {
		this.featurePermissions = featurePermissions;
	}
}

