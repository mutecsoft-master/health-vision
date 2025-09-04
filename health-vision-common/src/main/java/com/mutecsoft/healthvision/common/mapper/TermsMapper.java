package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.Terms;

@Mapper
public interface TermsMapper {

	List<Terms> selectLastVerTemrsListByLang(String lang);

	
}
