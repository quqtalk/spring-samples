package com.shqu.cas;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username, password;
	private boolean locked, expired, credentialExpired, disabled;
	private List<GrantedAuthority> authorities;

	public User(String username, String password, List<GrantedAuthority> authorities) {
		this(username, password, false, false, false, false, authorities);
	}

	public User(String username, String password, boolean locked, boolean disabled, boolean expired,
			boolean credentialExpired, List<GrantedAuthority> authorities) {
		this.username = username;
		this.password = password;
		this.credentialExpired = credentialExpired;
		this.expired = expired;
		this.disabled = disabled;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !this.expired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !this.locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !this.credentialExpired;
	}

	@Override
	public boolean isEnabled() {
		return !this.disabled;
	}

}
