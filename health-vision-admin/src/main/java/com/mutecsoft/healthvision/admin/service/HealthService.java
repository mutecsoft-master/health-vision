package com.mutecsoft.healthvision.admin.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mutecsoft.healthvision.common.dto.admin.AdminBgDto.SearchBgFile;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.BgFileResponse;

public interface HealthService {

	PageImpl<BgFileResponse> selectBgFileListPage(SearchBgFile searchParam, Pageable pageable);

	void downloadData(Long bgFileId, HttpServletResponse response);


}
