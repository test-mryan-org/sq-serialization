package com.swissquote.foundation.serialization.json.support.spring.amqp;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import com.swissquote.foundation.serialization.json.JsonObjectMapper;
import com.swissquote.foundation.serialization.json.JsonSerialization;
import com.swissquote.foundation.serialization.json.spi.JsonSerializationProvider;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SQJsonMessageConverter<N, P> extends AbstractJsonMessageConverter {

	@Setter
	private Type type;
	private TypeMapper typeMapper;
	private boolean typeMapperSet;

	private final JsonObjectMapper<N, P> jsonObjectMapper;

	public SQJsonMessageConverter() {
		this(JsonSerialization.getSerializationProvider());
	}

	public SQJsonMessageConverter(JsonSerializationProvider provider) {
		this.jsonObjectMapper = provider.getJsonObjectMapper();
		setTypeMapper(new TypeMapper());
	}

	public SQJsonMessageConverter(JsonObjectMapper<N, P> jsonObjectMapper) {
		this.jsonObjectMapper = jsonObjectMapper;
		setTypeMapper(new TypeMapper());
	}

	public void setTypeMapper(TypeMapper typeMapper) {
		this.typeMapper = typeMapper;
		typeMapper.setJsonObjectMapper(jsonObjectMapper);
		typeMapperSet = true;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		super.setBeanClassLoader(classLoader);
		if (!typeMapperSet) {
			typeMapper.setBeanClassLoader(classLoader);
		}
	}

	@Override
	protected Message createMessage(Object objectToConvert, MessageProperties messageProperties) {
		byte[] bytes;
		try {
			String jsonString = jsonObjectMapper.toJson(objectToConvert);
			bytes = jsonString.getBytes(getDefaultCharset());
		}
		catch (IOException e) {
			throw new MessageConversionException("Failed to convert Message content", e);
		}

		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		messageProperties.setContentEncoding(getDefaultCharset());
		messageProperties.setContentLength(bytes.length);
		// add contentType in header to be compliant with spring transformer
		messageProperties.getHeaders().put(TypeMapper.CONTENT_TYPE_NAME, MessageProperties.CONTENT_TYPE_JSON);

		if (getClassMapper() == null) {
			typeMapper.fromType(objectToConvert, messageProperties);
		} else {
			getClassMapper().fromClass(objectToConvert.getClass(), messageProperties);
		}

		return new Message(bytes, messageProperties);
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
					encoding = DEFAULT_CHARSET;
				}
				try {
					if (type != null) {
						content = convertBytesToObject(message.getBody(), encoding, type);
					} else if (getClassMapper() == null) {
						Type targetJavaType = typeMapper.toType(message.getMessageProperties());
						content = convertBytesToObject(message.getBody(), encoding, targetJavaType);
					} else {
						Class<?> targetClass = getClassMapper().toClass(message.getMessageProperties());
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

	private Object convertBytesToObject(byte[] body, String encoding, Type targetType) throws IOException {
		String contentAsString = new String(body, encoding);
		return jsonObjectMapper.fromJson(contentAsString, targetType);
	}

	private Object convertBytesToObject(byte[] body, String encoding, Class<?> targetClass) throws IOException {
		String contentAsString = new String(body, encoding);
		return jsonObjectMapper.fromJson(contentAsString, targetClass);
	}
}
