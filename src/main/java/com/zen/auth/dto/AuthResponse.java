package com.zen.auth.dto;

import java.util.List;

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
	public AuthResponse() {
		super();
	}
	public AuthResponse(String token,  String username2, String tenantId) {
		this.access_token = token;
		this.setUsername(tenantId);
		this.setUsername(username2);
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
	public void setOrgId(Long tenantId) {
		this.orgId = tenantId;
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


}
