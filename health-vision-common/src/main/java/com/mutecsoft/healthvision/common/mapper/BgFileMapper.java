package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.web.BgFileDto.BgFileResponse;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.BgFileSearchRequest;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.SearchBgFile;
import com.mutecsoft.healthvision.common.model.BgFile;

import java.util.List;

@Mapper
public interface BgFileMapper {
    void insertBgFile(BgFile bgFile);

    List<BgFile> selectBgFileList(SearchBgFile searchParam, Pageable pageable);

    int selectBgFileListCnt(SearchBgFile searchParam);

	List<BgFileResponse> selectBgFileListForApi(BgFileSearchRequest searchReq);
	
	BgFile selectBgFileById(Long bgFileId);
}
