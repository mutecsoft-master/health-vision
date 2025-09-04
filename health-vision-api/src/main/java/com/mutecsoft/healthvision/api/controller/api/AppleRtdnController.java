package com.mutecsoft.healthvision.api.controller.api;

import java.security.PublicKey;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutecsoft.healthvision.api.service.ReportService;
import com.mutecsoft.healthvision.api.util.AppleUtil;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.AppleRtdnDto;
import com.mutecsoft.healthvision.common.exception.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/appleRtdn")
@RequiredArgsConstructor
public class AppleRtdnController {

	private final ReportService reportService; 
	private final ObjectMapper objectMapper;
	
	private final AppleUtil appleUtil;
	private final WebClient webClient;
	
	//TODO[csm] 실서버 주소로 테스트 필요
	@PostMapping
	public ResponseEntity<Void> receiveRtdn(@RequestBody Map<String, Object> payload) {
		
		try {
			log.info("## Apple RTDN payload: {}", payload);

	        String signedPayload = (String) payload.get("signedPayload");
	        PublicKey publicKey = AppleUtil.extractPublicKeyFromX5c(signedPayload);

	        // 3. JWT 검증
	        Jws<Claims> jws = Jwts.parserBuilder()
	            .setSigningKey(publicKey)
	            .build()
	            .parseClaimsJws(signedPayload);

	        Claims claims = jws.getBody();
	        log.info("## Apple RTDN Verified Claims: {}", claims);

	        // AppleRtdnDto 변환
	        AppleRtdnDto payloadDto = objectMapper.convertValue(claims, AppleRtdnDto.class);
	        log.info("## Apple RTDN payloadDto: {}", payloadDto);
	        reportService.updateReportByAppleRtdn(payloadDto);

	    } catch (Exception e) {
	    	log.error("## RTDN parsing failed : {}", e.getMessage());
	    	return ResponseEntity.ok().build();
	    }

        return ResponseEntity.ok().build();
	}
	
	
	//테스트 알림 요청
	@PostMapping("/testRequest")
	public ResponseEntity<Void> testRequest() {
		
		String url = "https://api.storekit-sandbox.itunes.apple.com/inApps/v1/notifications/test";
		String jwt = appleUtil.getAppleJwtToken();
		String response = webClient.post()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(jwt))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .flatMap(body -> {
                                log.error("## Apple test request failed - status: {}, body: {}", clientResponse.statusCode(), body);
                                return Mono.error(new CustomException(ResultCdEnum.E001.getValue()));
                            })
                    )
                .bodyToMono(String.class)
                .block();

		log.info("## Apple notification test request : {}", response);
		
		return ResponseEntity.ok().build();
		
	}
	
	//테스트 알림 상태 확인
	@GetMapping("/testRequest/status")
	public ResponseEntity<Void> testRequestStatus(@RequestParam("testNotificationToken") String token) {
		
		String url = "https://api.storekit-sandbox.itunes.apple.com/inApps/v1/notifications/test/" + token;
		String jwt = appleUtil.getAppleJwtToken();
		String response = webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(jwt))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .flatMap(body -> {
                                log.error("## Apple test request status failed - status: {}, body: {}", clientResponse.statusCode(), body);
                                return Mono.error(new CustomException(ResultCdEnum.E001.getValue()));
                            })
                    )
                .bodyToMono(String.class)
                .block();

		log.info("## Apple notification test request status : {}", response);
		
		return ResponseEntity.ok().build();
		
	}
	
}
