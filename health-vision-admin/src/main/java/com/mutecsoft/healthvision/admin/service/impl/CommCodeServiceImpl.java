package com.mutecsoft.healthvision.admin.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mutecsoft.healthvision.admin.service.CommCodeService;
import com.mutecsoft.healthvision.common.exception.ResourceNotFoundException;
import com.mutecsoft.healthvision.common.mapper.CommCodeMapper;
import com.mutecsoft.healthvision.common.model.CommCode;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CommCodeServiceImpl implements CommCodeService {

    private final CommCodeMapper commCodeMapper;

    @Override
    public List<CommCode> getCommCodeList(CommCode commCode) {
        return commCodeMapper.getCommCodeList(commCode);
    }
    
    //단일 코드 수정/삭제시 검증용으로만 사용
    @Override
	public CommCode getCommCode(Long commCodeId) {
    	return commCodeMapper.getCommCode(commCodeId);
	}

    @Override
    public void insertCommCode(CommCode commCode) {
        commCodeMapper.insertCommCode(commCode);
    }

    @Override
    public void updateCommCodeByCodeId(CommCode commCode) {
    	validateCommCode(commCode.getCodeId());
        commCodeMapper.updateCommCodeByCodeId(commCode);

    }

    @Override
    public void deleteCommCode(CommCode commCode) {
    	
    	if(StringUtils.hasText(commCode.getCodeGroup())) {
    		validateCommCodeGroup(commCode);
    	}else {
    		validateCommCode(commCode.getCodeId());
    	}
        commCodeMapper.deleteCommCode(commCode);

    }
    
    //코드 그룹 검증
    @Override
    public void validateCommCodeGroup(CommCode commCode) {
    	List<CommCode> commCodeList = getCommCodeList(commCode);
    	    	
    	if(commCodeList.isEmpty()) {
        	throw new ResourceNotFoundException("CommCodeGroup", commCode.getCodeGroup());
        }
    }
    
    //단일 코드 검증
    @Override
    public void validateCommCode(Long commCodeId) {
    	CommCode commCode = getCommCode(commCodeId);
    	    	
    	if(commCode == null) {
        	throw new ResourceNotFoundException("CommCode", commCodeId.toString());
        }
        
    }
    
    

}
