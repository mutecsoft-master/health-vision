package com.mutecsoft.healthvision.common.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDto {
    
	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	public static class FileInsertDto {
		private int keyIndex;
		private MultipartFile file;
		private String cateCd;
	}
	
	
	@Getter
	@Setter
	@ToString
	public static class FileSearchDto {
		private Long fileId;
		private String userId;
	}
	
}
