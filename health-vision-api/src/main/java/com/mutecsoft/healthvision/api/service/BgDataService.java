package com.mutecsoft.healthvision.api.service;

import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.common.dto.web.BgFileDto.BgDataInsertDto;

@Service
public interface BgDataService {

    void insertBgDataList(BgDataInsertDto dto);
}
