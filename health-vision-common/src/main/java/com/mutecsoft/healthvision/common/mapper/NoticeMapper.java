package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.dto.NoticeDto.NoticeSearchRequest;
import com.mutecsoft.healthvision.common.model.Notice;

@Mapper
public interface NoticeMapper {

	List<Notice> selectNoticeList(NoticeSearchRequest searchReq);

	
}
