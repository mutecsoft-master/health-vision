package com.mutecsoft.healthvision.api.service;

import com.mutecsoft.healthvision.common.dto.HealthDto.HealthInfoRequest;
import com.mutecsoft.healthvision.common.dto.HealthDto.HealthInfoResponse;
import com.mutecsoft.healthvision.common.dto.HealthDto.HealthSaveDto;
import com.mutecsoft.healthvision.common.model.FinalUpload;

public interface HealthService {

	void saveHealth(HealthSaveDto healthDto);

	FinalUpload getFinalUpload();

	HealthInfoResponse getHealthInfo(HealthInfoRequest healthInfoReq);

}
