package com.mutecsoft.healthvision.api.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.androidpublisher.model.InAppProduct;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.mutecsoft.healthvision.api.service.AppleIapService;
import com.mutecsoft.healthvision.api.service.BgFileService;
import com.mutecsoft.healthvision.api.service.GoogleIapService;
import com.mutecsoft.healthvision.api.service.PaymentHistoryService;
import com.mutecsoft.healthvision.api.service.ReportV2Service;
import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.constant.ApplePurchaseStatusEnum;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.GooglePurchaseStatusEnum;
import com.mutecsoft.healthvision.common.constant.PlatformEnum;
import com.mutecsoft.healthvision.common.constant.ReportPurchaseStatusCdEnum;
import com.mutecsoft.healthvision.common.constant.ReportV2StatusCdEnum;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.constant.TransactionTypeEnum;
import com.mutecsoft.healthvision.common.dto.AppleRtdnDto;
import com.mutecsoft.healthvision.common.dto.AppleTransactionInfoDto;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.AppleRtdnDto.Data;
import com.mutecsoft.healthvision.common.dto.IapDto.GooglePurchaseRequest;
import com.mutecsoft.healthvision.common.dto.PaymentHistoryDto.SearchRequestForAppleRdtn;
import com.mutecsoft.healthvision.common.dto.PaymentHistoryDto.SearchRequestForGoogleRdtn;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.PurchaseReportAppleRequest;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.PurchaseReportGoogleRequest;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportApplyRequest;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportResponse;
import com.mutecsoft.healthvision.common.dto.ReportV2Dto.ReportSearchRequest;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.ReportV2Mapper;
import com.mutecsoft.healthvision.common.model.BgFile;
import com.mutecsoft.healthvision.common.model.PaymentHistory;
import com.mutecsoft.healthvision.common.model.Report;
import com.mutecsoft.healthvision.common.util.FileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReportV2ServiceImpl implements ReportV2Service {
	
	@Value("${report-preview-url}")
    private String reportPreviewUrl;
	
	@Value("${report-download-url}")
    private String reportDownloadUrl;

    private final UserUtil userUtil;
    private final BgFileService bgFileService;
    private final GoogleIapService googleIapService;
    private final AppleIapService appleIapService;
    private final ReportV2Mapper reportMapper;
    private final PaymentHistoryService paymentHistoryService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    
    @Override
	public ResponseDto applyReport(ReportApplyRequest applyReq) {
		UserInfo userInfo = userUtil.getUserInfo();
		
		BgFile bgFile = bgFileService.getBgFileById(applyReq.getBgFileId());
		if(bgFile == null) {
			throw new CustomException(ResultCdEnum.E001.getValue());
		}
		
		//bgFile 소유자 검증
		if(!bgFile.getUserId().equals(userInfo.getUserId())) {
			throw new CustomException(ResultCdEnum.E002.getValue());
		}
		
		ReportResponse registeredReport = reportMapper.selectReportByBgFileId(applyReq.getBgFileId());
		if(registeredReport != null) {
			throw new CustomException(ResultCdEnum.R001.getValue());
		}
		
		Report report = new Report();
		report.setAplyId(userInfo.getUserId());
		report.setAplyDt(LocalDateTime.now());
		report.setStatusCd(ReportV2StatusCdEnum.APPLY.getValue());
		report.setPurchaseStatusCd(ReportPurchaseStatusCdEnum.UNPAID.getValue());
		report.setBgFileId(bgFile.getBgFileId());
		reportMapper.insertReport(report);
		return new ResponseDto(true);
	}
    
	@Override
	public ResponseDto purchaseReportForGoogle(PurchaseReportGoogleRequest purchaseReq) {
		
		ProductPurchase verifyPurchase = googleIapService.verifyPurchase(purchaseReq.getPurchase());
		
		if(verifyPurchase.getPurchaseState() == Integer.parseInt(GooglePurchaseStatusEnum.PURCHASED.getValue())
				|| verifyPurchase.getPurchaseState() == Integer.parseInt(GooglePurchaseStatusEnum.PENDING.getValue())) {

			//결제 이력 저장
			PaymentHistory ph = new PaymentHistory();
			ph.setTransactionId(verifyPurchase.getOrderId());
			ph.setTransactionType(TransactionTypeEnum.PAYMENT.getValue());
			ph.setPurchaseToken(purchaseReq.getPurchase().getPurchaseToken());
			ph.setProductId(purchaseReq.getPurchase().getProductId());
			
			Long purchaseTimestamp = verifyPurchase.getPurchaseTimeMillis();
			LocalDateTime purchaseDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(purchaseTimestamp), TimeZone.getDefault().toZoneId());
			ph.setPaymentDt(purchaseDt);
			ph.setPlatform(PlatformEnum.GOOGLE.getValue());
			ph.setStatus(GooglePurchaseStatusEnum.fromValue(String.valueOf(verifyPurchase.getPurchaseState())).getValue());
			try {
				//상품 정보 조회 중 오류가 발생해도 결제 이력 저장
				InAppProduct productInfo = googleIapService.getProductInfo(purchaseReq.getPurchase());
				BigDecimal amount = new BigDecimal(productInfo.getDefaultPrice().getPriceMicros()).divide(BigDecimal.valueOf(1_000_000));
				ph.setAmount(amount);
				ph.setCurrency(productInfo.getDefaultPrice().getCurrency());
				
			} catch (Exception e) {
				log.error("## Failed to get google prodcut info", e.getMessage());
			}
			
			Report report = reportMapper.selectReportById(purchaseReq.getReportId());
			if(verifyPurchase.getPurchaseState() == Integer.parseInt(GooglePurchaseStatusEnum.PURCHASED.getValue())) {
				report.setPurchaseStatusCd(ReportPurchaseStatusCdEnum.COMPLETE.getValue());
				reportMapper.updateReportPurchaseStatus(report);
			}else {
				//일단 대기로 저장하고, 이후 google에서 오는 알림을 이용해 처리
				report.setPurchaseStatusCd(ReportPurchaseStatusCdEnum.PENDING.getValue());
				reportMapper.updateReportPurchaseStatus(report);
			}
			
			ph.setUserId(report.getAplyId());
			ph.setReportId(report.getReportId());
			paymentHistoryService.insertPaymentHistory(ph);
			
			return new ResponseDto(true);
		}else {
			throw new CustomException(ResultCdEnum.E008.getValue(), String.valueOf(verifyPurchase.getPurchaseState()));
		}
	}
	
	@Override
	public ResponseDto purchaseReportForApple(PurchaseReportAppleRequest purchaseReq) {
		
		try {
			Map<String, String> verifyMap = appleIapService.verifyReceipt(purchaseReq.getPurchase());
			
			if(!verifyMap.isEmpty()) {
				
				String trInfoStr = verifyMap.get("signedTransactionInfo");
				String[] parts = trInfoStr.split("\\.");
				String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
				AppleTransactionInfoDto trInfoDto = objectMapper.readValue(payloadJson, AppleTransactionInfoDto.class);
				
				log.info("## signedTransactionInfo : {}", trInfoStr);
				log.info("## payloadJson : {}", payloadJson);
				
				//결제 이력 저장
				PaymentHistory ph = new PaymentHistory();
				ph.setTransactionId(trInfoDto.getTransactionId());
				ph.setTransactionType(TransactionTypeEnum.PAYMENT.getValue());
				ph.setProductId(trInfoDto.getProductId());
				
				Long purchaseTimestamp = trInfoDto.getPurchaseDate();
				LocalDateTime purchaseDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(purchaseTimestamp), TimeZone.getDefault().toZoneId());
				ph.setPaymentDt(purchaseDt);
				ph.setPlatform(PlatformEnum.APPLE.getValue());
				ph.setStatus(ApplePurchaseStatusEnum.PURCHASED.getValue());
				BigDecimal amount = new BigDecimal(trInfoDto.getPrice()).divide(BigDecimal.valueOf(1_000), 2, RoundingMode.HALF_UP);
				ph.setAmount(amount);
				ph.setCurrency(trInfoDto.getCurrency());
					
				//애플은 소모성 구매에 대해 별도 알림을 제공하지 않으므로 바로 '결제완료'상태 적용
				Report report = reportMapper.selectReportById(purchaseReq.getReportId());
				report.setPurchaseStatusCd(ReportPurchaseStatusCdEnum.COMPLETE.getValue());
				reportMapper.updateReportPurchaseStatus(report);
				
				ph.setUserId(report.getAplyId());
				ph.setReportId(report.getReportId());
				paymentHistoryService.insertPaymentHistory(ph);
					
				return new ResponseDto(true);
			}else {
				throw new CustomException(ResultCdEnum.E008.getValue());
			}
		} catch (JsonProcessingException e) {
			throw new CustomException(ResultCdEnum.E008.getValue());
		}
	}
	
	@Override
	public List<ReportResponse> getReportList(Integer limitMonth) {
		UserInfo userInfo = userUtil.getUserInfo();
		
		ReportSearchRequest searchReq = new ReportSearchRequest();
		searchReq.setUserId(userInfo.getUserId());
		searchReq.setLimitMonth(limitMonth);
		
		List<ReportResponse> reportList = reportMapper.selectReportList(searchReq);
		
		List<ReportResponse> dtoList = reportList.stream()
				.map(r -> {
					ReportResponse dto = modelMapper.map(r, ReportResponse.class);
					dto.setReportPreviewUrl(fileUtil.makeReportApiUrl(reportPreviewUrl, dto.getReportFileId()));
		            dto.setReportDownloadUrl(fileUtil.makeReportApiUrl(reportDownloadUrl, dto.getReportFileId()));
		            return dto;
		        })
				.collect(Collectors.toList());
		
		return dtoList;
	}
	
	@Override
	public void updateReportByGoogleRtdn(JSONObject json) {
		try {
			
			JSONObject noti = new JSONObject();
			if(json.has(Const.GOOGLE_RTDN_NOTI_ONE_TIME)) { //일회성 구매
				noti = json.getJSONObject(Const.GOOGLE_RTDN_NOTI_ONE_TIME);
				String purchaseToken = noti.has("purchaseToken") ? noti.getString("purchaseToken") : "";
				
				Report report = reportMapper.selectReportByPurchaseToken(purchaseToken);
				
				if(report != null) {
					//기존 paymentHistory 조회
					SearchRequestForGoogleRdtn searchReq = new SearchRequestForGoogleRdtn();
					searchReq.setPurchaseToken(purchaseToken);
					searchReq.setTransactionType(TransactionTypeEnum.PAYMENT.getValue());
					PaymentHistory ph = paymentHistoryService.selectPaymentHistoryForGoogleRdtn(searchReq);
					
					GooglePurchaseRequest purchaseReq = new GooglePurchaseRequest();
					purchaseReq.setPackageName(json.getString("packageName"));
					purchaseReq.setProductId(ph.getProductId());
					purchaseReq.setPurchaseToken(purchaseToken);
					
					ProductPurchase verifyPurchase = googleIapService.verifyPurchase(purchaseReq);
					if(noti.getInt("notificationType") == 1) { //구매 성공(1)
						//검증 후 상태 업데이트(리포트, 결제이력)
						if(verifyPurchase.getPurchaseState() == Integer.parseInt(GooglePurchaseStatusEnum.PURCHASED.getValue())) {
							report.setPurchaseStatusCd(ReportPurchaseStatusCdEnum.COMPLETE.getValue());
							reportMapper.updateReportPurchaseStatus(report);
							
							ph.setStatus(GooglePurchaseStatusEnum.PURCHASED.getValue());
							ph.setTransactionId(verifyPurchase.getOrderId());
							paymentHistoryService.updatePaymentHistory(ph);
						}
					}else { //구매 취소(2)
						//결제이력 업데이트
						if(verifyPurchase.getPurchaseState() == Integer.parseInt(GooglePurchaseStatusEnum.CANCELED.getValue())) {
							report.setPurchaseStatusCd(ReportPurchaseStatusCdEnum.UNPAID.getValue());
							reportMapper.updateReportPurchaseStatus(report);
							
							ph.setStatus(GooglePurchaseStatusEnum.CANCELED.getValue());
							paymentHistoryService.updatePaymentHistory(ph);
						}
					}
				}
				
			}else if(json.has(Const.GOOGLE_RTDN_NOTI_VOIDED)) { //구매 무효
				noti = json.getJSONObject(Const.GOOGLE_RTDN_NOTI_VOIDED);
				if(noti.getInt("refundType") == 1) { // 구매 완전 취소(1)
					String orderId = noti.getString("orderId");
					String purchaseToken = noti.has("purchaseToken") ? noti.getString("purchaseToken") : "";
					Report report = reportMapper.selectReportByPurchaseToken(purchaseToken);
					
					if(report != null) {
						SearchRequestForGoogleRdtn searchReq = new SearchRequestForGoogleRdtn();
						searchReq.setPurchaseToken(purchaseToken);
						searchReq.setTransactionType(TransactionTypeEnum.PAYMENT.getValue());
						PaymentHistory ph = paymentHistoryService.selectPaymentHistoryForGoogleRdtn(searchReq);
						
						GooglePurchaseRequest purchaseReq = new GooglePurchaseRequest();
						purchaseReq.setPackageName(json.getString("packageName"));
						purchaseReq.setProductId(ph.getProductId());
						purchaseReq.setPurchaseToken(purchaseToken);
						ProductPurchase verifyPurchase = googleIapService.verifyPurchase(purchaseReq);
						
						if(verifyPurchase.getPurchaseState() == Integer.parseInt(GooglePurchaseStatusEnum.CANCELED.getValue())) {
							
							report.setPurchaseStatusCd(ReportPurchaseStatusCdEnum.UNPAID.getValue());
							reportMapper.updateReportPurchaseStatus(report);
							
							ph.setTransactionId(orderId);
							ph.setTransactionType(TransactionTypeEnum.REFUND.getValue());
							Long refundTimestamp = json.getLong("eventTimeMillis");
							LocalDateTime refundDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(refundTimestamp), TimeZone.getDefault().toZoneId());
							ph.setRefundDt(refundDt);
							ph.setStatus(GooglePurchaseStatusEnum.CANCELED.getValue());
							paymentHistoryService.insertPaymentHistory(ph);
						}
					}
				}
				
			}else {
				log.info("## Undefined google rtdn : {}", json);
			}
			
	    } catch (Exception e) {
	        log.error("## Google RTDN 처리 중 예외", e);
	        throw new CustomException(ResultCdEnum.E008.getValue());
	    }
	}

	@Override
	public void updateReportByAppleRtdn(AppleRtdnDto payloadDto) {
		
		try {
			String notificationType = payloadDto.getNotificationType();
			if(StringUtils.hasText(notificationType)) {
				
				Data data = payloadDto.getData();
				String trInfoStr = data.getSignedTransactionInfo();
				String[] parts = trInfoStr.split("\\.");
				String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
				AppleTransactionInfoDto trInfoDto = objectMapper.readValue(payloadJson, AppleTransactionInfoDto.class);
				
				SearchRequestForAppleRdtn searchReq = new SearchRequestForAppleRdtn();
				searchReq.setTransactionType(TransactionTypeEnum.PAYMENT.getValue());
				searchReq.setTransactionId(trInfoDto.getTransactionId());
				PaymentHistory ph = paymentHistoryService.selectPaymentHistoryForAppleRdtn(searchReq);
				
				//트랜잭션 검증
				LocalDateTime purchaseDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(trInfoDto.getPurchaseDate()), TimeZone.getDefault().toZoneId());
				if(!trInfoDto.getTransactionId().equals(ph.getTransactionId())
						|| !trInfoDto.getProductId().equals(ph.getProductId())
						|| !purchaseDt.equals(ph.getPaymentDt())) {
					
					log.error("## Apple RTDN 구매정보와 일치하지 않음");
					throw new CustomException(ResultCdEnum.E008.getValue());
				}
				
				Report report = reportMapper.selectReportById(ph.getReportId());
				if(report != null) {
					if(notificationType.equals("REFUND")) { //환불
						
						report.setPurchaseStatusCd(ReportPurchaseStatusCdEnum.UNPAID.getValue());
						reportMapper.updateReportPurchaseStatus(report);
						
						ph.setTransactionType(TransactionTypeEnum.REFUND.getValue());
						LocalDateTime refundDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(trInfoDto.getRevocationDate()), TimeZone.getDefault().toZoneId());
						ph.setRefundDt(refundDt);
						ph.setRefundReason(String.valueOf(trInfoDto.getRevocationReason()));
						ph.setStatus(ApplePurchaseStatusEnum.CANCELED.getValue());
						paymentHistoryService.updatePaymentHistory(ph);
						
					}
				}
		 		
			}
			
		}catch (Exception e) {
			log.error("## Apple RTDN 처리 중 예외", e);
	        throw new CustomException(ResultCdEnum.E008.getValue());
		}
		
	}

	@Override
	public Report getReportByReportFileId(Long fileId) {
		return reportMapper.selectReportByReportId(fileId);
	}

}
