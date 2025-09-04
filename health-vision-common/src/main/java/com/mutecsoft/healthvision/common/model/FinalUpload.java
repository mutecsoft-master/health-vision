package com.mutecsoft.healthvision.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinalUpload {
	
	private Long finalUploadId;
	private LocalDateTime uploadDt;
	private Long userId;
	
}
