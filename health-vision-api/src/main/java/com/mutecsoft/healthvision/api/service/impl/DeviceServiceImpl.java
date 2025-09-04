package com.mutecsoft.healthvision.api.service.impl;

import com.mutecsoft.healthvision.api.service.DeviceService;
import com.mutecsoft.healthvision.common.mapper.DeviceMapper;
import com.mutecsoft.healthvision.common.model.Device;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;

    @Override
    public void insertDevice(Device device) {
        deviceMapper.insertDevice(device);
    }
}
