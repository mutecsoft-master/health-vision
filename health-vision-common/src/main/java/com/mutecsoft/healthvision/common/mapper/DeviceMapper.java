package com.mutecsoft.healthvision.common.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.model.Device;

@Mapper
public interface DeviceMapper {

    void insertDevice(Device device);
}
