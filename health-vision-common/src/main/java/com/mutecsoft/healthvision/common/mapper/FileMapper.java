package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.annotation.LoginId;
import com.mutecsoft.healthvision.common.model.FileModel;

@Mapper
public interface FileMapper {

	@LoginId
	void insertFile(FileModel fileModel);
	
	FileModel selectFile(Long fileId);

	void deleteFile(Long fileId);
	
}
