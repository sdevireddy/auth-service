package com.zen.auth.dto;

import java.util.List;

public class RoleCreateDTO {
    private String name;
    private List<Long> permissionIds;
    private List<FeaturePermissionDTO> featurePermissions;
    private List<FieldPermissionDTO> fieldPermissions;
    private List<Long> bundleIds;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Long> getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}
	public List<FeaturePermissionDTO> getFeaturePermissions() {
		return featurePermissions;
	}
	public void setFeaturePermissions(List<FeaturePermissionDTO> featurePermissions) {
		this.featurePermissions = featurePermissions;
	}
	public List<FieldPermissionDTO> getFieldPermissions() {
		return fieldPermissions;
	}
	public void setFieldPermissions(List<FieldPermissionDTO> fieldPermissions) {
		this.fieldPermissions = fieldPermissions;
	}
	public List<Long> getBundleIds() {
		return bundleIds;
	}
	public void setBundleIds(List<Long> bundleIds) {
		this.bundleIds = bundleIds;
	}
    

}
