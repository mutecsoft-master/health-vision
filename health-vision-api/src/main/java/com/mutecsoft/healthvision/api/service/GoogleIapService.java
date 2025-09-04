package com.mutecsoft.healthvision.api.service;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.InAppProduct;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.IapDto.GooglePurchaseRequest;
import com.mutecsoft.healthvision.common.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleIapService {

    private final AndroidPublisher androidPublisher;

    public ProductPurchase verifyPurchase(GooglePurchaseRequest googleIap) {
        try {
			return androidPublisher.purchases().products()
			        .get(googleIap.getPackageName(),
		        		googleIap.getProductId(),
		        		googleIap.getPurchaseToken())
			        .execute();
		} catch (IOException e) {
			throw new CustomException(ResultCdEnum.E008.getValue());
		}
		
    }
    
    public InAppProduct getProductInfo(GooglePurchaseRequest googleIap) throws IOException {
        AndroidPublisher.Inappproducts.Get request = androidPublisher.inappproducts()
            .get(googleIap.getPackageName(), googleIap.getProductId());
        
        return request.execute();
    }

	public void handleRtdn(JSONObject json) {
		
	}

}