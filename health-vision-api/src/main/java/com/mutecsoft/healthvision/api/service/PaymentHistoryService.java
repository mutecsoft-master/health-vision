package com.mutecsoft.healthvision.api.service;

import com.mutecsoft.healthvision.common.dto.PaymentHistoryDto.SearchRequestForAppleRdtn;
import com.mutecsoft.healthvision.common.dto.PaymentHistoryDto.SearchRequestForGoogleRdtn;
import com.mutecsoft.healthvision.common.model.PaymentHistory;

public interface PaymentHistoryService {

	void insertPaymentHistory(PaymentHistory ph);

	PaymentHistory selectPaymentHistoryForGoogleRdtn(SearchRequestForGoogleRdtn searchReq);
	PaymentHistory selectPaymentHistoryForAppleRdtn(SearchRequestForAppleRdtn searchReq);

	void updatePaymentHistory(PaymentHistory ph);

	


}
