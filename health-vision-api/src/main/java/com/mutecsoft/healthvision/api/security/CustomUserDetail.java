package com.mutecsoft.healthvision.api.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mutecsoft.healthvision.common.model.User;

import lombok.Data;

@Data
public class CustomUserDetail implements UserDetails {

	private Long userId;
    private String email;
    private String userPw;
    private String lockYn;
    private List<String> roleNmList;
    
    public CustomUserDetail(User user) {
		this.userId = user.getUserId();
		this.email = user.getEmail();
		this.userPw = user.getUserPw();
		this.lockYn = user.getLockYn();
		this.roleNmList = user.getRoleNmList();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roleNmList.stream()
	            .map(roleNm -> new SimpleGrantedAuthority("ROLE_" + roleNm))
	            .collect(Collectors.toList());
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
		return !"Y".equalsIgnoreCase(lockYn);
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
