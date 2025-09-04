package com.mutecsoft.healthvision.api.service;

import java.util.List;

import com.mutecsoft.healthvision.common.model.Notice;

public interface NoticeService {

	List<Notice> selectNoticeList(int size, Long lastNoticeId);
}
