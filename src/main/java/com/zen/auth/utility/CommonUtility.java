package com.zen.auth.utility;

public class CommonUtility {
	
	 public static String extractTenantsSuffix(String email) {
	        String domain = email.substring(email.indexOf("@") + 1); // e.g. orgname.com
	        String tenant = domain.split("\\.")[0]; // gets "orgname"
	        return tenant;
	    }

}
