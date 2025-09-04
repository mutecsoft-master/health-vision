package com.mutecsoft.healthvision.api.service;

import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.common.model.Device;

@Service
public interface DeviceService {

    void insertDevice(Device device);
}
