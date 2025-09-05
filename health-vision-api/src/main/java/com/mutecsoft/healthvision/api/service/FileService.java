package com.mutecsoft.healthvision.api.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.mutecsoft.healthvision.common.model.FileModel;

public interface FileService {

	void insertFile(FileModel fm);
	
	FileModel selectFile(Long fileId);

	ResponseEntity<?> getFile(Long fileId, HttpServletResponse response);
	
	void deleteFile(Long fileId) throws IOException;

}
