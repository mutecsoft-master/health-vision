package com.mutecsoft.healthvision.admin.service.impl;

import com.mutecsoft.healthvision.admin.service.NoticeService;
import com.mutecsoft.healthvision.admin.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.NoticeDto.NoticeInfo;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.CreateNotice;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.SearchNotice;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.UpdateNotice;
import com.mutecsoft.healthvision.common.mapper.ANoticeMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final UserUtil userUtil;
    private final ANoticeMapper aNoticeMapper;

    @Override
    public PageImpl<NoticeInfo> selectNoticeListPage(SearchNotice searchParam, Pageable pageable) {

        List<NoticeInfo> noticeList = aNoticeMapper.selectNoticeList(searchParam, pageable);
        int totalCnt = aNoticeMapper.selectNoticeListCnt(searchParam, pageable);

        return new PageImpl<>(noticeList, pageable, totalCnt);
    }

    @Override
    public void createNotice(CreateNotice createNotice) {
        UserInfo userInfo = userUtil.getUserInfo();
        createNotice.setRegId(userInfo.getUserId());

        aNoticeMapper.createNotice(createNotice);
    }

    @Override
    public void updateNotice(UpdateNotice updateNotice) {
        UserInfo userInfo = userUtil.getUserInfo();
        updateNotice.setUpdId(userInfo.getUserId());

        aNoticeMapper.updateNotice(updateNotice);
    }

    @Override
    public void deleteNotices(List<Long> noticeIdList) {
        aNoticeMapper.deleteNotices(noticeIdList);
    }

}
