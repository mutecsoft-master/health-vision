package com.mutecsoft.healthvision.admin.service;

import java.util.List;

import com.mutecsoft.healthvision.common.exception.ResourceNotFoundException;
import com.mutecsoft.healthvision.common.model.CommCode;

public interface CommCodeService {

    List<CommCode> getCommCodeList(CommCode commCode);
    
    CommCode getCommCode(Long commCodeId);

    void insertCommCode(CommCode commCode);

    void updateCommCodeByCodeId(CommCode commCode);

    void deleteCommCode(CommCode commCode);
    
    void validateCommCodeGroup(CommCode commCode);
    
    void validateCommCode(Long commCodeId);

}
