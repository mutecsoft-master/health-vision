package com.mutecsoft.healthvision.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.api.service.FileService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.FileCateCdEnum;
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
		fileMapper.insertFile(fileModel);
	}
	
	@Override
	public FileModel selectFile(Long fileId) {
		return fileMapper.selectFile(fileId);
	}
	
	@Override
	public ResponseEntity<?> getFile(Long fileId, HttpServletResponse response) {
		FileModel fileModel = fileMapper.selectFile(fileId);
		UserInfo userInfo = userUtil.getUserInfo();
		
		if(fileModel != null) {
			//권한 체크
			if(StringUtils.hasText(fileModel.getFileCateCd())) {
				
				FileCateCdEnum fileCateCdEnum = FileCateCdEnum.fromValue(fileModel.getFileCateCd());
				
				switch (fileCateCdEnum) {
					case FOOD: case PRESET: case PROFILE: {
						//소유자 체크
						if(!userInfo.getUserId().equals(fileModel.getRegId())) {
							throw new RuntimeException("접근 권한이 없습니다.");
						}
						break;
					}
					
					default: 
						break;
				}
			}
			
			File file = new File(basePath + "/" + fileModel.getFilePath());
		    if (!file.exists()) {
		    	throw new RuntimeException("파일을 찾을 수 없습니다.");
		    }

		    try {
		        Resource resource = new FileSystemResource(file);
		        
		        // 파일의 MIME 타입을 동적으로 설정
		        String contentType = Files.probeContentType(file.toPath());
		        if (contentType == null) {
		            contentType = "application/octet-stream"; // 알 수 없는 경우 기본값 설정
		        }

		        return ResponseEntity.ok()
		                .contentType(MediaType.parseMediaType(contentType))
		                .body(resource);
		    } catch (IOException e) {
		    	throw new RuntimeException("파일을 다운로드 할 수 없습니다.");
		    }
		}else {
			throw new RuntimeException("파일을 찾을 수 없습니다.");
		}
		
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
