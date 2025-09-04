package com.mutecsoft.healthvision.api.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
public class AppleIapConfig {

	@Value("${apple.iap.key-id}")
    private String keyId;

    @Value("${apple.iap.issuer-id}")
    private String issuerId;

    @Value("${apple.iap.key-path}")
    private String keyPath;
    
    @Value("${apple.iap.bundle-id}")
    private String bundleId;

    @Bean(name = "applePrivateKey")
    ECPrivateKey applePrivateKey() throws Exception {
    	Resource resource = new ClassPathResource(keyPath);
    	String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s+", ""); //줄바꿈, 공백, 탭 등 제거

        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return (ECPrivateKey) kf.generatePrivate(spec);
    }

}