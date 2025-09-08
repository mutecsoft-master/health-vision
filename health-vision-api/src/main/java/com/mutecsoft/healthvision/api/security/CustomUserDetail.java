package com.mutecsoft.healthvision.api.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mutecsoft.healthvision.common.model.User;

import lombok.Data;

@Data
public class CustomUserDetail implements UserDetails {

	private Long userId;
    private String email;
    private String userPw;
    
    public CustomUserDetail(User user) {
		this.userId = user.getUserId();
		this.email = user.getEmail();
		this.userPw = user.getUserPw();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return userPw;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	

}
