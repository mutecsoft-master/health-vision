package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.FinalUpload;

@Mapper
public interface FinalUploadMapper {
	
	void insertFinalUpload(FinalUpload model);

	FinalUpload getFinalUpload(Long userId);
	
}
