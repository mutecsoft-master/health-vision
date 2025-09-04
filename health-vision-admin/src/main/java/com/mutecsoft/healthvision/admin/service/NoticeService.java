package com.mutecsoft.healthvision.admin.service;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.NoticeDto.NoticeInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.CreateNotice;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.SearchNotice;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.UpdateNotice;

import java.util.List;

public interface NoticeService {

    PageImpl<NoticeInfo> selectNoticeListPage(SearchNotice searchParam, Pageable pageable);

    void createNotice(CreateNotice createNotice);

    void updateNotice(UpdateNotice updateNotice);

    void deleteNotices(List<Long> noticeIdList);

}
