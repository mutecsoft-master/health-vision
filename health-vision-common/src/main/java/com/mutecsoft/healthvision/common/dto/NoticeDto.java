package com.mutecsoft.healthvision.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

import com.mutecsoft.healthvision.common.model.BaseModel;

@Getter
@Setter
@ToString
public class NoticeDto {
	
	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
    public static class NoticeSearchRequest{
		private Long lastNoticeId;
		private int size;
		private String lang;
		
		public NoticeSearchRequest(Long lastNoticeId, int size, String lang) {
			this.lastNoticeId = lastNoticeId;
			this.size = size;
			this.lang = lang;
		}
    }

	@Getter
	@Setter
	@ToString
	public static class NoticeInfo extends BaseModel {
		private Long noticeId;
		private String title;
		private String content;
		private String lang;
		private String delYn;

		private LocalDate regDate;
		private LocalDate updDate;
	}
}
