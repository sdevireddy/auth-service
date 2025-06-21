package com.zen.auth.filters;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.zen.auth.common.entity.ZenUser;




public class ZenUserDetails implements UserDetails {

	    private final ZenUser user;

	    public ZenUserDetails(ZenUser user) {
	        this.user = user;
	    }

	    public boolean isFirstLogin() {
	        return user.isFirstLogin(); // Assumes getter in ZenUser
	    }

	    public ZenUser getUser() {
	        return this.user;
	    }

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return Collections.emptyList(); // Add role mapping if needed
	    }

	    @Override
	    public String getPassword() {
	        return user.getPassword();
	    }

	    @Override
	    public String getUsername() {
	        return user.getUsername();
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true; // Customize if needed
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	    	if (user.getAccountLocked() == null) {
	    		return true;
	    	} else {
	    		return !user.getAccountLocked();
	    	}
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true; // Customize if needed
	    }

	    @Override
	    public boolean isEnabled() {
	        return user.getIsActive(); // Assumes getIsActive() returns boolean
	    }

		public List<String> getRoleNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public List<String> getModules() {
			// TODO Auto-generated method stub
			return null;
		}
	}