package com.mutecsoft.healthvision.api.service;

import java.util.List;

import com.mutecsoft.healthvision.common.model.Terms;

public interface TermsService {

	List<Terms> selectLastVerTemrsList(String lang);
}
