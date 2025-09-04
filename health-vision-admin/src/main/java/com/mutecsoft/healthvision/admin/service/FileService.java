package com.mutecsoft.healthvision.admin.service;

import java.io.IOException;

import com.mutecsoft.healthvision.common.model.FileModel;

public interface FileService {

	void insertFile(FileModel fm);
	
	FileModel selectFile(Long fileId);

	void deleteFile(Long fileId) throws IOException;

}
