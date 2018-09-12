package com.swissquote.foundation.serialization.json.support.spring.amqp;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissquote.foundation.serialization.json.JacksonJsonObjectMapper;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SQJsonMessageConverter implements MessageConverter {

	@Setter
	private Type type;

	private final Jackson2JsonMessageConverter jackson2JsonMessageConverter;
	private final ObjectMapper objectMapper;

	public SQJsonMessageConverter() {
		this(JacksonJsonObjectMapper.standardObjectMapper());
	}

	public SQJsonMessageConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		jackson2JsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);
	}

	@Override
	public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
		return jackson2JsonMessageConverter.toMessage(object, messageProperties);
	}

	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
		Object content = null;
		MessageProperties properties = message.getMessageProperties();
		if (properties != null) {
			String contentType = properties.getContentType();
			if (contentType != null && contentType.contains("json")) {
				String encoding = properties.getContentEncoding();
				if (encoding == null) {
					encoding = jackson2JsonMessageConverter.getDefaultCharset();
				}
				try {
					if (type != null) {
						JavaType targetJavaType =
								objectMapper.getTypeFactory().constructType(type);
						content = convertBytesToObject(message.getBody(), encoding, targetJavaType);
					} else if (jackson2JsonMessageConverter.getClassMapper() == null) {
						JavaType targetJavaType =
								jackson2JsonMessageConverter.getJavaTypeMapper().toJavaType(message.getMessageProperties());
						content = convertBytesToObject(message.getBody(), encoding, targetJavaType);
					} else {
						Class<?> targetClass =
								jackson2JsonMessageConverter.getClassMapper().toClass(message.getMessageProperties());
						content = convertBytesToObject(message.getBody(), encoding, targetClass);
					}
				}
				catch (IOException e) {
					throw new MessageConversionException("Failed to convert Message content", e);
				}
			} else {
				log.warn("Could not convert incoming message with content-type [" + contentType + "], 'json' keyword missing.");
			}
		}
		if (content == null) {
			content = message.getBody();
		}
		return content;
	}

	private Object convertBytesToObject(byte[] body, String encoding, JavaType targetJavaType) throws IOException {
		String contentAsString = new String(body, encoding);
		return objectMapper.readValue(contentAsString, targetJavaType);
	}

	private Object convertBytesToObject(byte[] body, String encoding, Class<?> targetClass) throws IOException {
		String contentAsString = new String(body, encoding);
		return objectMapper.readValue(contentAsString, objectMapper.constructType(targetClass));
	}
}
