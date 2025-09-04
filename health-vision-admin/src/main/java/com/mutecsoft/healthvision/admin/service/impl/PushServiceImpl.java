package com.mutecsoft.healthvision.admin.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.ErrorCode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.mutecsoft.healthvision.admin.service.PushService;
import com.mutecsoft.healthvision.admin.service.TokenService;
import com.mutecsoft.healthvision.common.constant.Const;
import com.mutecsoft.healthvision.common.constant.PushTypeEnum;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.PushDto;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.mapper.PushMapper;
import com.mutecsoft.healthvision.common.model.Push;
import com.mutecsoft.healthvision.common.model.PushFail;
import com.mutecsoft.healthvision.common.model.PushTarget;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushServiceImpl implements PushService {

	@Value("${firebase.key-path}")
    private String firebaseKeyPath;
	
	private final TokenService tokenService;
	
	private final PushMapper pushMapper;

	@PostConstruct
    public void initFirebase() {
        if (FirebaseApp.getApps().isEmpty()) {
        	
        	ClassPathResource resource = new ClassPathResource(firebaseKeyPath);
            try (InputStream serviceAccountStream = resource.getInputStream()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                        .build();

                FirebaseApp.initializeApp(options);
            } catch (IOException e) {
            	log.error("## filebase 초기화 실패 - {}", e.getMessage());
            	throw new CustomException(ResultCdEnum.E001.getValue());
			}
        }
    }

	@Override
	public void sendPush(Long userId, PushTypeEnum pushTypeEnum, PushDto pushDto) {
		sendPushMulticast(List.of(userId), pushTypeEnum, pushDto);
	}

	@Transactional
	@Override
	public void sendPushMulticast(List<Long> userIdList, PushTypeEnum pushTypeEnum, PushDto pushDto) {
		
		List<String> totalFcmTokenList = tokenService.selectFcmTokenList(userIdList);
		
		if(totalFcmTokenList.size() > 0) {
			List<List<String>> dividedFcmTokenList = divideFcmTokenList(totalFcmTokenList);
			
			List<String> failTokenList = new ArrayList<>();
			List<FirebaseMessagingException> exceptionList = new ArrayList<>();
			int totalSuccessCnt = 0;
			
			for(List<String> fcmTokenList : dividedFcmTokenList) {
				MulticastMessage message = MulticastMessage.builder()
					.setNotification(Notification.builder()
							.setTitle(pushDto.getTitle())
							.setBody(pushDto.getBody())
							.build())
					.putData("title", pushDto.getTitle())
					.putData("body", pushDto.getBody())
				    .addAllTokens(fcmTokenList)
				    .build();
				BatchResponse response;
				try {
					response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
					if (response.getFailureCount() > 0) {
					List<SendResponse> responses = response.getResponses();
					  for (int i = 0; i < responses.size(); i++) {
					    if (!responses.get(i).isSuccessful()) {
					      failTokenList.add(fcmTokenList.get(i));
					      exceptionList.add(responses.get(i).getException());
					      
					      MessagingErrorCode errorCode = responses.get(i).getException().getMessagingErrorCode();

					      //발송되지 않는 토큰은 제거 - 추후 Batch 로 전환 검토
					      if(errorCode == MessagingErrorCode.UNREGISTERED 
					    		  || errorCode == MessagingErrorCode.INVALID_ARGUMENT
					    		  || errorCode == MessagingErrorCode.SENDER_ID_MISMATCH) {
					    	  
					    	  tokenService.deleteFcmToken(fcmTokenList.get(i));
					    	  
					      }
					    }
					  }
					}
					totalSuccessCnt += response.getSuccessCount();
					
				} catch (FirebaseMessagingException e) {
					log.error("## FCM Push Batch 실패 : {}", e.getMessage());
				}
			}
			
			log.info("## Push 실패 토큰 목록 : {}", failTokenList);
			log.info("## Push 성공 count : {}", totalSuccessCnt);
			
			//Push
			Push push = new Push();
			push.setTitle(pushDto.getTitle());
			push.setBody(pushDto.getBody());
			push.setType(pushTypeEnum.getValue());
			push.setTargetCnt(totalFcmTokenList.size());
			push.setSuccessCnt(totalSuccessCnt);
			push.setFailCnt(failTokenList.size());
			pushMapper.insertPush(push);
			
			//Push target
			List<PushTarget> pushTargetList = new ArrayList<>();
			for(Long userId : userIdList) {
				PushTarget pushTarget = new PushTarget();
				pushTarget.setPushId(push.getPushId());
				pushTarget.setUserId(userId);
				pushTargetList.add(pushTarget);
			}
			pushMapper.insertPushTargetList(pushTargetList);
			
			//Push fail history
			if(failTokenList.size() != 0) {
				List<PushFail> pushFailList = new ArrayList<>();
				for(int i=0; i<failTokenList.size(); i++) {
					PushFail pushFail = new PushFail();
					pushFail.setPushId(push.getPushId());
					pushFail.setToken(failTokenList.get(i));
					
					if(exceptionList.size() != 0 && exceptionList.get(i) != null) {
						pushFail.setErrorCd(exceptionList.get(i).getErrorCode().name());
						pushFail.setErrorDesc(exceptionList.get(i).getMessage());
					}
					
					pushFailList.add(pushFail);
				}
				pushMapper.insertPushFailList(pushFailList);
			}
		}
	}
	
	
	private List<List<String>> divideFcmTokenList(List<String> fcmTokenList) {
		List<List<String>> dividedTokenList = new ArrayList<>();
		List<String> tempList = new ArrayList<>();
		
		for(int i=0; i<fcmTokenList.size(); i++) {
			tempList.add(fcmTokenList.get(i));
			
			if(tempList.size() == Const.MAX_FCM_TOKEN_COUNT || (i+1) == fcmTokenList.size()) {
				dividedTokenList.add(tempList);
				tempList = new ArrayList<>();
			}
		}
		
		return dividedTokenList;
	}
}
