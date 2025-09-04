package com.mutecsoft.healthvision.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.mutecsoft.healthvision.common.dto.PaymentHistoryDto.SearchRequestForAppleRdtn;
import com.mutecsoft.healthvision.common.dto.PaymentHistoryDto.SearchRequestForGoogleRdtn;
import com.mutecsoft.healthvision.common.model.PaymentHistory;

@Mapper
public interface PaymentHistoryMapper {
	
	void insertPaymentHistory(PaymentHistory ph);

	PaymentHistory selectPaymentHistoryForGoogleRdtn(SearchRequestForGoogleRdtn searchReq);
	PaymentHistory selectPaymentHistoryForAppleRdtn(SearchRequestForAppleRdtn searchReq);

	void updatePaymentHistory(PaymentHistory ph);

	
	
}
