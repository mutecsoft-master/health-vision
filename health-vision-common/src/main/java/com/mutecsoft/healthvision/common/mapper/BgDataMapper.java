package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.BgData;

import java.util.List;

@Mapper
public interface BgDataMapper {

    void insertBgData(List<BgData> bgDataList);
}
