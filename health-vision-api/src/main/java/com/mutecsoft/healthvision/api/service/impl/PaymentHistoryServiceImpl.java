package com.mutecsoft.healthvision.api.service.impl;

import org.springframework.stereotype.Service;

import com.mutecsoft.healthvision.api.service.PaymentHistoryService;
import com.mutecsoft.healthvision.common.dto.PaymentHistoryDto.SearchRequestForAppleRdtn;
import com.mutecsoft.healthvision.common.dto.PaymentHistoryDto.SearchRequestForGoogleRdtn;
import com.mutecsoft.healthvision.common.mapper.PaymentHistoryMapper;
import com.mutecsoft.healthvision.common.model.PaymentHistory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentHistoryServiceImpl implements PaymentHistoryService {
	
	private final PaymentHistoryMapper paymentHistoryMapper;
	
	@Override
	public void insertPaymentHistory(PaymentHistory ph) {
		paymentHistoryMapper.insertPaymentHistory(ph);
	}

	@Override
	public PaymentHistory selectPaymentHistoryForGoogleRdtn(SearchRequestForGoogleRdtn searchReq) {
		return paymentHistoryMapper.selectPaymentHistoryForGoogleRdtn(searchReq);
	}
	
	@Override
	public PaymentHistory selectPaymentHistoryForAppleRdtn(SearchRequestForAppleRdtn searchReq) {
		return paymentHistoryMapper.selectPaymentHistoryForAppleRdtn(searchReq);
	}

	@Override
	public void updatePaymentHistory(PaymentHistory ph) {
		paymentHistoryMapper.updatePaymentHistory(ph);
	}

	


}
