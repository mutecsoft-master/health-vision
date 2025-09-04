package com.mutecsoft.healthvision.common.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.mutecsoft.healthvision.common.annotation.Password;
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
		@Email(message = "V001")
		private String email;
		@NotEmpty
		@Password(message = "V002")
		private String userPw;
		@NotEmpty
		List<Long> agreeTermsIdList;
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
		@Email(message = "V001")
		private String email;
    }
	
	@Getter
	@Setter
	@ToString
    public static class NicknameRequest{
		@NotEmpty
		private String nickname;
    }
	
	@Getter
	@Setter
	@ToString
    public static class EmailAuthRequest{
		@NotEmpty
		@Email(message = "V001")
		private String email;
		@NotEmpty
		private String code;
    }
	
	@Getter
	@Setter
	@ToString
    public static class OauthLoginRequest{
		@NotEmpty
		private String idToken;
    }
	
	@Getter
	@Setter
	@ToString
    public static class PwRequest{
		
		@NotEmpty
		private String currentPw;
		
		@NotEmpty
		@Password(message = "V002")
		private String newPw;
    }
	
	@Getter
	@Setter
	@ToString
    public static class UserSearchRequest{
		private Long userId;
		private String email;
    }
	
	@Getter
	@Setter
	@ToString
    public static class UserUpdateRequest{
		private Long userId;
	    private String email;
	    private String gender;
	    private String birthDate;
	    private Double height;
	    private Double weight;
	    private String nickname;
	    private Long profileFileId;
	    
	    private MultipartFile profileFile;
	    
    }
	

	@Getter
	@Setter
	@ToString
    public static class UserInfo extends BaseModel implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private Long userId;
	    private String email;
	    private String snsProvider;
	    private String snsUserId;
	    private String gender;
	    private String birthDate;
	    private Double height;
	    private Double weight;
	    private String nickname;
	    private Long profileFileId;
	    private LocalDateTime lastLoginDt;
	    private String delYn;
	    private LocalDateTime delDt;
	    
	    private List<String> roleNmList;
	    private String profileImgUrl;
	    
	    private String regDate;
    }
    
}
