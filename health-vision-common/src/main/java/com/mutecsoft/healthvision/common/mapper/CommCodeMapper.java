package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.CommCode;


@Mapper
public interface CommCodeMapper {

	List<CommCode> getCommCodeList(CommCode commCode);
	
	CommCode getCommCode(Long commCodeId);

	void insertCommCode(CommCode commCode);

	void updateCommCodeByCodeId(CommCode commCode);

	void deleteCommCode(CommCode commCode);

}
