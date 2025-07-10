package com.zen.auth.filters;

public class TenantContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        contextHolder.set(tenantId);
    }

    public static String getTenantId() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }

	public TenantContextHolder() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}

