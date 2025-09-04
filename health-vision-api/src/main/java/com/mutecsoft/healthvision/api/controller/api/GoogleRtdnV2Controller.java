package com.mutecsoft.healthvision.api.controller.api;

import java.util.Base64;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.ReportV2Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/googleRtdn")
@RequiredArgsConstructor
public class GoogleRtdnV2Controller {

	private final ReportV2Service reportService; 
	
	@PostMapping("/v2")
	public ResponseEntity<Void> receiveRtdn(@RequestBody Map<String, Object> payload) {
		
		try {
			Map<String, Object> message = (Map<String, Object>) payload.get("message");
	        String encodedData = (String) message.get("data");
	        String decodedData = new String(Base64.getDecoder().decode(encodedData));
	
	        JSONObject json = new JSONObject(decodedData);
	        log.info("## RTDN payload: {}", payload);
	        log.info("## RTDN data: {}", json.toString());
			
	        reportService.updateReportByGoogleRtdn(json);
	        
		} catch (Exception e) {
			log.error("## RTDN parsing failed : {}", e.getMessage());
		}
		
		return ResponseEntity.ok().build();
	}
	
	
}
