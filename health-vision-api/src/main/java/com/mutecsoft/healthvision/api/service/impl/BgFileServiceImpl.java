package com.mutecsoft.healthvision.api.service.impl;

import com.mutecsoft.healthvision.api.processor.BgFileProcessor;
import com.mutecsoft.healthvision.api.service.BgDataService;
import com.mutecsoft.healthvision.api.service.BgFileService;
import com.mutecsoft.healthvision.api.service.DeviceService;
import com.mutecsoft.healthvision.api.util.BgFileUtil;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.CsvDto.BgFileParseResult;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.*;
import com.mutecsoft.healthvision.common.mapper.BgFileMapper;
import com.mutecsoft.healthvision.common.model.BgFile;
import com.mutecsoft.healthvision.common.model.Device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BgFileServiceImpl implements BgFileService {

    // Util
    private final UserUtil userUtil;
    private final BgFileUtil bgFileUtil;

    // Service
    private final BgDataService bgDataService;
    private final DeviceService deviceService;

    // Mapper
    private final BgFileMapper bgFileMapper;

    // ETC
    private final BgFileProcessor bgFileProcessor;

    @Override
    public void bgFileUpload(List<MultipartFile> files) {
        UserInfo userInfo = userUtil.getUserInfo();

        for (MultipartFile file : files) {

            // 1. 파일 타입에 맞춰 데이터 추출
            BgFileParseResult parseData = bgFileProcessor.parseByType(file);

            // 2. BgFile 저장
            BgFileInsertDto bgFileInsertDto = new BgFileInsertDto();
            bgFileInsertDto.setUserId(userInfo.getUserId());
            bgFileInsertDto.setFile(file);
            bgFileInsertDto.setRecordStartDate(parseData.getRecordStartDate());
            bgFileInsertDto.setRecordEndDate(parseData.getRecordEndDate());
            BgFile bgFile = bgFileUtil.saveBgFile(bgFileInsertDto);
            bgFileMapper.insertBgFile(bgFile);

            // 3. Device 저장
            Device device = new Device();
            device.setDeviceNm(parseData.getDeviceName());
            deviceService.insertDevice(device);

            // 4. tb_bg_data 테이블에 데이터 정보 저장
            BgDataInsertDto bgDataInsertDto = new BgDataInsertDto();
            bgDataInsertDto.setBgDataList(parseData.getBgDataList());
            bgDataInsertDto.setUnit(parseData.getUnit());
            bgDataInsertDto.setBgFileId(bgFile.getBgFileId());
            bgDataInsertDto.setDeviceId(device.getDeviceId());
            bgDataService.insertBgDataList(bgDataInsertDto);
        }
    }

    @Override
    public PageImpl<BgFile> getBgFileList(Pageable pageable, SearchBgFile searchParam) {
        UserInfo userInfo = userUtil.getUserInfo();
        searchParam.setUserId(userInfo.getUserId());

        int totalCnt = bgFileMapper.selectBgFileListCnt(searchParam);
        List<BgFile> bgFileList = bgFileMapper.selectBgFileList(searchParam, pageable);

        return new PageImpl<>(bgFileList, pageable, totalCnt);
    }

    
    
    
    
    
	@Override
	public List<BgFileResponse> getBgFileListForApi(Integer limit) {
		UserInfo userInfo = userUtil.getUserInfo();
		
		BgFileSearchRequest searchReq = new BgFileSearchRequest();
		searchReq.setUserId(userInfo.getUserId());
		searchReq.setLimit(limit);
		
		List<BgFileResponse> bgFileList = bgFileMapper.selectBgFileListForApi(searchReq);
		
		return bgFileList;
	}

	@Override
	public BgFile getBgFileById(Long bgFileId) {
		return bgFileMapper.selectBgFileById(bgFileId);
	}
}
