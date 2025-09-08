package com.mutecsoft.healthvision.common.dto.admin;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminUserDto {
	
	@Getter
	@Setter
	@ToString
    public static class SearchUser {
		
		//검색 파라미터
		private String searchEmail;
		private String searchDelYn;
		private String searchRegDate;
    }
    
	
	@Getter
	@Setter
	@ToString
	public static class RegisterAnalystRequest {
		@NotEmpty
		private String email;
		
		@NotEmpty
		private String userPw;
	}
	
	@Getter
	@Setter
	@ToString
    public static class UserUpdateRequest{
		private Long userId;
	    private String email;
	    private String userPw;
	    
    }
}
