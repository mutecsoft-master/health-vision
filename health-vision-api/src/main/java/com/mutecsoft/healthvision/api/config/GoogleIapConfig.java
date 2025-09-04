package com.mutecsoft.healthvision.api.config;

import java.io.InputStream;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.mutecsoft.healthvision.common.constant.Const;

@Configuration
public class GoogleIapConfig {

    @Value("${google.play.credentials-path}")
    private String credentialsPath;

    @Bean
    AndroidPublisher androidPublisher() throws Exception {
        // JSON 키 로드
    	Resource resource = new ClassPathResource(credentialsPath);
    	InputStream credentialsStream = resource.getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));

        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        return new AndroidPublisher.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                jsonFactory,
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName(Const.APP_NAME).build();
    }
}