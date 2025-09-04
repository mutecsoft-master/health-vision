package com.mutecsoft.healthvision.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutecsoft.healthvision.api.util.AppleUtil;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.IapDto.ApplePurchaseRequest;
import com.mutecsoft.healthvision.common.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleIapService {
	
	@Value("${apple.iap.is-sandbox}")
	private boolean isSandbox;
	
	@Value("${apple.iap.verify-url.production}")
    private String productionUrl;

    @Value("${apple.iap.verify-url.sandbox}")
    private String sandboxUrl;

	private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AppleUtil appleUtil;

    public Map<String, String> verifyReceipt(ApplePurchaseRequest appleIap) {
    	try {
	        String baseUrl  = isSandbox ? sandboxUrl : productionUrl;
	        String url = baseUrl + "/" + appleIap.getTransactionId();
	        
	        String jwt = appleUtil.getAppleJwtToken();
	        
	        String response = webClient.get()
                    .uri(url)
                    .headers(headers -> headers.setBearerAuth(jwt))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> {
                                    log.error("## Apple purchase request failed - status: {}, body: {}", clientResponse.statusCode(), body);
                                    return Mono.error(new CustomException(ResultCdEnum.E008.getValue()));
                                })
                        )
                    .bodyToMono(String.class)
                    .block();
	
	        Map<String, String> body = objectMapper.readValue(response, Map.class);
	        log.info("## Apple purchase result : {}", body);
	        
	        return body;
			
		} catch (JsonProcessingException e) {
			throw new CustomException(ResultCdEnum.E008.getValue());
		} catch (Exception e) {
		    throw new CustomException(ResultCdEnum.E008.getValue());
		}
            
    }
}