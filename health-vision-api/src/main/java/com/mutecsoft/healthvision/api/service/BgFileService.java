package com.mutecsoft.healthvision.api.service;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mutecsoft.healthvision.common.dto.web.BgFileDto.BgFileResponse;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.SearchBgFile;
import com.mutecsoft.healthvision.common.model.BgFile;

import java.util.List;

@Service
public interface BgFileService {

    void bgFileUpload(List<MultipartFile> files);

    PageImpl<BgFile> getBgFileList(Pageable pageable, SearchBgFile searchParam);

    
    
	List<BgFileResponse> getBgFileListForApi(Integer limit);
	
	BgFile getBgFileById(Long bgFileId);
}
