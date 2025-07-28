package com.zen.auth.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ZenTenantDTO {

	private Long orgId;

	@NotBlank(message = "Organization name is required")
	@Size(max = 60, message = "Organization name must not exceed 60 characters")
	@Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Organization name can only contain letters, numbers, and underscores")
	private String orgName;

	@NotBlank(message = "Username (email) is required")
	@Email(message = "Invalid email format")
	private String userName;

	@NotBlank(message = "Admin name is required")
	@Size(max = 100, message = "Admin name must not exceed 100 characters")
	private String adminName;

	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	private String password;

	@NotEmpty(message = "At least one module must be selected")
	private List<@NotBlank String> modules;

	public List<String> getModuleKeys() {
		return modules;
	}

	public void setModuleKeys(List<String> modules) {
		this.modules = modules;
	}
	// --- Getters and Setters ---

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public ZenTenantDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
