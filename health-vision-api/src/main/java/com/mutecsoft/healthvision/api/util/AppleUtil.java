package com.mutecsoft.healthvision.api.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppleUtil {

    private final WebClient webClient;
    
    //login
    @Value("${apple.login.auth-url}")
    private String appleAuthUrl;
    
    @Value("${apple.login.bundle-id}")
    private String appleBundleId;
    
    @Value("${apple.login.jwk-set-uri}")
    private String appleJwkSetUri;
    
    //iap
    @Value("${apple.iap.key-id}")
    private String keyId;

    @Value("${apple.iap.issuer-id}")
    private String issuerId;

    @Value("${apple.iap.key-path}")
    private String keyPath;
    
    @Value("${apple.iap.bundle-id}")
    private String bundleId;
    
    @Qualifier("applePrivateKey")
    private final ECPrivateKey applePrivateKey;

    
    //Apple 로그인 관련 Start
    public String createClientSecret(Map<String, String> authMap) {

        String teamId = authMap.get("appleTeamId");
        String clientId = authMap.get("appleClientId");
        String keyId = authMap.get("appleKeyId");
        String keyPath = authMap.get("appleKeyPath");
        String authUrl = authMap.get("appleAuthUrl");

        Date now = new Date();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(keyId).build();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(teamId)
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + 24 * 60 * 60 * 1000))
                .audience(authUrl)
                .subject(clientId)
                .build();

        SignedJWT jwt = new SignedJWT(header, claimsSet);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            byte[] encodedKey = readPrivateKey(keyPath);
            KeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
            ECPrivateKey ecPrivateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);
            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey);
            jwt.sign(jwsSigner);

        } catch (JOSEException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("## createClientSecret error - {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        return jwt.serialize();
    }


    private byte[] readPrivateKey(String keyPath) {

        Resource resource = new ClassPathResource(keyPath);
        byte[] content = null;

        try (InputStreamReader keyReader = new InputStreamReader(resource.getInputStream());
    	     PemReader pemReader = new PemReader(keyReader)) {
            {
                PemObject pemObject = pemReader.readPemObject();
                content = pemObject.getContent();
            }
        } catch (IOException e) {
            log.error("## readPrivateKey error - {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        return content;
    }

    public String getAppleToken(String url, Map<String, String> param) {
        // Map을 MultiValueMap으로 변환
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.setAll(param);
        
        String result = null;

        try {
            result = webClient.mutate()
                    .baseUrl(url)
                    .defaultHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .build()
            		.post()
                    .uri("") // baseUrl에 전체 URL을 넣었기 때문에 uri는 비움
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .onStatus(
                        status -> !status.is2xxSuccessful(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("## error : {}", errorBody);
                                return Mono.error(new CustomException(errorBody));
                            })
                    )
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("## doPost WebClientResponseException - {}: {}", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw new CustomException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("## doPost error - {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        return result;
    }
    
    //TODO[csm] web 로그인용으로 사용중인데, 검증 절차가 빠져있음. 사용하지 말 것
    @Deprecated
    public JSONObject decodeFromIdToken(String idToken) {

    	try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            JWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();

            String appleInfo = JSONObjectUtils.toJSONObject(getPayload).toJSONString();

            return new JSONObject(appleInfo);
    	} catch (ParseException e) {
    		new JSONObject();
			return null;
		}

    }

    public JSONObject decodeAndValidateIdToken(String idToken) {

    	try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
           
            String jwkSetUri = appleJwkSetUri;
            
            //1. 공개키 로드 및 서명 검증
            JWKSet jwkSet = JWKSet.load(new URL(jwkSetUri));
            JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());
            
            if (jwk == null) {
            	log.error("## JWT no matching");
            	throw new CustomException(ResultCdEnum.L102.getValue());
            }
            
            JWSVerifier verifier = new RSASSAVerifier(((RSAKey)jwk).toRSAPublicKey());
            if (!signedJWT.verify(verifier)) {
            	log.error("## Signature verification failed");
            	throw new CustomException(ResultCdEnum.L102.getValue());
            }
            
            // 2. 클레임 검증
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date now = new Date();
            
            if (!appleAuthUrl.equals(claims.getIssuer())) {
            	log.error("## Invalid issuer");
            	throw new CustomException(ResultCdEnum.L102.getValue());
            }
            if (!claims.getAudience().contains(appleBundleId)) {
            	log.error("## Invalid audience");
            	throw new CustomException(ResultCdEnum.L102.getValue());
            	
            }
            if (now.after(claims.getExpirationTime())) {
                log.error("## Token expired audience");
                throw new CustomException(ResultCdEnum.L102.getValue());
            }
            
            String jsonStr = JSONObjectUtils.toJSONObject(claims).toJSONString();
            return new JSONObject(jsonStr);
    	} catch (Exception e) {
    		log.error("## apple idToken validation error : {}", e.getMessage());
    		return null;
		}

    }
    
    
    //Apple 인앱결제 관련 Start
    public static PublicKey extractPublicKeyFromX5c(String jwt) {
    	try {
	        String[] parts = jwt.split("\\.");
	        if (parts.length != 3) {
	        	log.error("## Invalid JWT format");
	        	throw new CustomException(ResultCdEnum.E001.getValue());
	        }
	
	        // 헤더 디코딩
	        String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
	        JSONObject header = new JSONObject(headerJson);
	
	        JSONArray x5cArray = header.getJSONArray("x5c");
	        if (x5cArray.isEmpty()) {
	        	log.error("## x5c not found in header");
	        	throw new CustomException(ResultCdEnum.E001.getValue());
	        }
	
	        String x5c = x5cArray.getString(0);
	
	        // x5c를 X.509 인증서로 변환
	        byte[] certBytes = Base64.getDecoder().decode(x5c);
	        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
	        Certificate certificate = certFactory.generateCertificate(new ByteArrayInputStream(certBytes));
	
	        return certificate.getPublicKey();
    	} catch (CertificateException e) {
			throw new CustomException(ResultCdEnum.E001.getValue());
		}
    }
    
    public String getAppleJwtToken() {
        Instant now = Instant.now();
        return Jwts.builder()
            .setHeaderParam("alg", "ES256")
            .setHeaderParam("typ", "JWT")
            .setHeaderParam("kid", keyId)
            .setIssuer(issuerId)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(1800)))
            .setAudience("appstoreconnect-v1")
            .claim("bid", bundleId)
            .signWith(applePrivateKey, SignatureAlgorithm.ES256)
            .compact();
    }
}
