package com.swissquote.foundation.serialization.json.support.spring.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.config.EnableIntegration;

@Configuration
@EnableIntegration
@ImportResource("classpath:/test-context.xml")
public class Context {

	@Bean
	public SQJsonObjectMapper objectMapper() {
		return new SQJsonObjectMapper();
	}

	@Bean
	public OutboundEndpoint outboundEndpoint() {
		return new OutboundEndpoint();
	}

}
