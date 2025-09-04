package com.mutecsoft.healthvision.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.Push;
import com.mutecsoft.healthvision.common.model.PushFail;
import com.mutecsoft.healthvision.common.model.PushTarget;

@Mapper
public interface PushMapper {

	void insertPush(Push push);

	void insertPushTargetList(List<PushTarget> pushTargetList);

	void insertPushFailList(List<PushFail> pushFailList);
    
}
