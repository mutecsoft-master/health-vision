package com.mutecsoft.healthvision.admin.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.admin.service.FileService;
import com.mutecsoft.healthvision.admin.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.mapper.FileMapper;
import com.mutecsoft.healthvision.common.model.FileModel;
import com.mutecsoft.healthvision.common.util.FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
	
	private final FileMapper fileMapper;
	private final UserUtil userUtil;
	private final FileUtil fileUtil;
	
	@Value("${file.base-path}")
    private String basePath;
	
	    
	@Override
	public void insertFile(FileModel fileModel) {
		UserInfo userInfo = userUtil.getUserInfo();
		fileModel.setRegId(userInfo.getUserId());
		fileModel.setUpdId(userInfo.getUserId());
		fileMapper.insertFile(fileModel);
	}
	
	@Override
	public FileModel selectFile(Long fileId) {
		return fileMapper.selectFile(fileId);
	}
	
	@Override
	public void deleteFile(Long fileId) throws IOException {
		
		FileModel fileModel = fileMapper.selectFile(fileId);
		
		if(fileModel != null) {
			//1.실제 파일 삭제
			if(StringUtils.hasText(fileModel.getFilePath())) {
				fileUtil.deleteFile(fileModel.getFilePath());
			}
			
		    //2. DB 데이터 삭제
		    fileMapper.deleteFile(fileId);
		}
		
	}


}
