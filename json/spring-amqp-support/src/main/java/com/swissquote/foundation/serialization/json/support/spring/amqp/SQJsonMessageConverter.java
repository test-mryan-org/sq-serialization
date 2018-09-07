package com.swissquote.foundation.serialization.json.support.spring.amqp;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.util.ClassUtils;

import com.swissquote.foundation.serialization.json.JsonObjectMapper;
import com.swissquote.foundation.serialization.json.JsonSerialization;
import com.swissquote.foundation.serialization.json.spi.JsonSerializationProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SQJsonMessageConverter<N, P> implements MessageConverter {

	private final JsonObjectMapper<N, P> jsonObjectMapper;

	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String DEFAULT_CLASSID_FIELD_NAME = "__TypeId__";
	private static final String DEFAULT_HASHTABLE_TYPE_ID = "Hashtable";

	private volatile Class<?> defaultHashtableClass = Hashtable.class;
	private volatile String defaultCharset = DEFAULT_CHARSET;
	private boolean createMessageIds = false;

	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

	public void setCreateMessageIds(boolean createMessageIds) {
		this.createMessageIds = createMessageIds;
	}

	public SQJsonMessageConverter() {
		this(JsonSerialization.getSerializationProvider());
	}

	public SQJsonMessageConverter(JsonSerializationProvider provider) {
		this.jsonObjectMapper = provider.getJsonObjectMapper();
	}

	public SQJsonMessageConverter(JsonObjectMapper<N, P> jsonObjectMapper) {
		this.jsonObjectMapper = jsonObjectMapper;
	}

	@Override
	public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
		if (messageProperties == null) {
			messageProperties = new MessageProperties();
		}
		Message message = createMessage(object, messageProperties);
		messageProperties = message.getMessageProperties();
		if (createMessageIds && messageProperties.getMessageId() == null) {
			messageProperties.setMessageId(UUID.randomUUID().toString());
		}
		return message;
	}

	private Message createMessage(Object objectToConvert, MessageProperties messageProperties)
			throws MessageConversionException {
		byte[] bytes;
		try {
			String jsonString = jsonObjectMapper.toJson(objectToConvert);
			bytes = jsonString.getBytes(defaultCharset);
		}
		catch (IOException e) {
			throw new MessageConversionException("Failed to convert Message content", e);
		}
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		messageProperties.setContentEncoding(defaultCharset);
		if (bytes != null) {
			messageProperties.setContentLength(bytes.length);
		}
		messageProperties.getHeaders().put(DEFAULT_CLASSID_FIELD_NAME, fromClass(objectToConvert.getClass()));

		return new Message(bytes, messageProperties);
	}

	private Object fromClass(Class<?> aClass) {
		if (Map.class.isAssignableFrom(aClass)) {
			return DEFAULT_HASHTABLE_TYPE_ID;
		}
		return aClass.getName();
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
					encoding = defaultCharset;
				}
				try {
					Class<?> targetClass = toClass(message.getMessageProperties());
					content = jsonObjectMapper.fromJson(new String(message.getBody(), encoding), targetClass);
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

	private Class<?> toClass(MessageProperties properties) {
		Map<String, Object> headers = properties.getHeaders();
		Object classIdFieldNameValue = headers.get(DEFAULT_CLASSID_FIELD_NAME);
		String classId = null;
		if (classIdFieldNameValue != null) {
			classId = classIdFieldNameValue.toString();
		}
		if (classId == null) {
			throw new MessageConversionException("failed to convert Message content. Could not resolve " +
					DEFAULT_CLASSID_FIELD_NAME + " in header and no defaultType provided");
		}

		if (classId.equals(DEFAULT_HASHTABLE_TYPE_ID)) {
			return this.defaultHashtableClass;
		}
		try {
			return ClassUtils.forName(classId, getClass().getClassLoader());
		}
		catch (ClassNotFoundException | LinkageError e) {
			throw new MessageConversionException("failed to resolve class name [" + classId + "]", e);
		}
	}

}
