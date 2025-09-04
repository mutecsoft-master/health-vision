package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.NoticeDto.NoticeInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.CreateNotice;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.SearchNotice;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.UpdateNotice;

import java.util.List;

@Mapper
public interface ANoticeMapper {

    List<NoticeInfo> selectNoticeList(SearchNotice searchParam, Pageable pageable);

    int selectNoticeListCnt(SearchNotice searchParam, Pageable pageable);

    void createNotice(CreateNotice createNotice);

    void updateNotice(UpdateNotice updateNotice);

    void deleteNotices(List<Long> noticeIdList);

    void restoreNotices(List<Long> noticeIdList);
}
