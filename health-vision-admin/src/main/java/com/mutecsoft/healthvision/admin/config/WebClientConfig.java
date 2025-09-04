package com.mutecsoft.healthvision.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
@Slf4j
public class WebClientConfig {
	
	@Bean
	WebClient webClient() {
		//timeout
		HttpClient httpClient = HttpClient.create();
//				  .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
//				  .responseTimeout(Duration.ofMillis(10000));
//				  .doOnConnected(conn -> 
//				    conn.addHandlerLast(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
//				      .addHandlerLast(new WriteTimeoutHandler(10000, TimeUnit.MILLISECONDS)));
		
		//http message size
		ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024*1024*10))
                .build();
		
		//logging
		exchangeStrategies
			.messageWriters().stream()
			.filter(LoggingCodecSupport.class::isInstance)
			.forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));
		
		WebClient client = WebClient
				.builder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.exchangeStrategies(exchangeStrategies)
                .filter(ExchangeFilterFunction.ofRequestProcessor(
                    clientRequest -> {
                        log.info("WebClient Request: {} {}", clientRequest.method(), clientRequest.url());
                        clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
                        return Mono.just(clientRequest);
                    }
                ))
                .filter(ExchangeFilterFunction.ofResponseProcessor(
                    clientResponse -> {
                        clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
                        return Mono.just(clientResponse);
                    }
                ))
				.build();
		
		return client;
	}
	
}
