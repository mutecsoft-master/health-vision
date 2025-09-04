package com.mutecsoft.healthvision.api.service.impl;

import com.mutecsoft.healthvision.api.service.BgDataService;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.CsvDto.BgDataDto;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.BgDataInsertDto;
import com.mutecsoft.healthvision.common.mapper.BgDataMapper;
import com.mutecsoft.healthvision.common.model.BgData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BgDataServiceImpl implements BgDataService {

    private final UserUtil userUtil;
    private final BgDataMapper bgDataMapper;

    @Override
    public void insertBgDataList(BgDataInsertDto dto) {
        UserInfo userInfo = userUtil.getUserInfo();
        List<BgData> bgDataList = new ArrayList<>();

        for (BgDataDto data : dto.getBgDataList()) {
            BgData bgData = new BgData();
            bgData.setUnit(dto.getUnit());
            bgData.setValue(data.getGlucoseValue());
            bgData.setRecordDt(data.getTimestamp());
            bgData.setDeviceId(dto.getDeviceId());
            bgData.setUserId(userInfo.getUserId());
            bgData.setBgFileId(dto.getBgFileId());
            bgDataList.add(bgData);
        }

        bgDataMapper.insertBgData(bgDataList);
    }
}
