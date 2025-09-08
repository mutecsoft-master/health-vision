package com.mutecsoft.healthvision.common.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import com.mutecsoft.healthvision.common.model.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
	
	@Getter
	@Setter
	@ToString
    public static class SignupRequest{
		@NotEmpty
		private String email;
		@NotEmpty
		private String userPw;
    }
	
	@Getter
	@Setter
	@ToString
    public static class LoginRequest{
		@NotEmpty
		private String email;
		@NotEmpty
		private String userPw;
    }
	
	@Getter
	@Setter
	@ToString
    public static class EmailRequest{
		@NotEmpty
		private String email;
    }
	
	@Getter
	@Setter
	@ToString
    public static class UserInfo extends BaseModel implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private Long userId;
	    private String email;
	    private LocalDateTime lastLoginDt;
	    private String delYn;
	    private LocalDateTime delDt;
    }
    
}
