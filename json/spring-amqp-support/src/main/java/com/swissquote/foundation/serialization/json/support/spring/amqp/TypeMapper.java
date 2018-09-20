package com.swissquote.foundation.serialization.json.support.spring.amqp;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.util.ClassUtils;

import com.swissquote.foundation.serialization.json.JsonObjectMapper;

import lombok.Setter;

public class TypeMapper implements ClassMapper, BeanClassLoaderAware {

	static final String CONTENT_TYPE_NAME = "contentType";
	private static final String CLASS_ID_FIELD_NAME = "__TypeId__";
	private static final String CONTENT_CLASS_ID_FIELD_NAME = "__ContentTypeId__";
	private static final String KEY_CLASS_ID_FIELD_NAME = "__KeyTypeId__";

	@Setter
	private JsonObjectMapper jsonObjectMapper;

	private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
	private final Map<String, Class<?>> idClassMapping = new HashMap<>();
	private final Map<Class<?>, String> classIdMapping = new HashMap<>();

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void setIdClassMapping(Map<String, Class<?>> idClassMapping) {
		idClassMapping.putAll(idClassMapping);
		createReverseMap();
	}

	private void createReverseMap() {
		classIdMapping.clear();
		for (Map.Entry<String, Class<?>> entry : idClassMapping.entrySet()) {
			String id = entry.getKey();
			Class<?> clazz = entry.getValue();
			classIdMapping.put(clazz, id);
		}
	}

	private Type getClassIdType(String classId) {
		if (idClassMapping.containsKey(classId)) {
			return idClassMapping.get(classId);
		}
		try {
			return ClassUtils.forName(classId, classLoader);
		}
		catch (ClassNotFoundException e) {
			throw new MessageConversionException("failed to resolve class name. Class not found [" + classId + "]", e);
		}
	}

	@Override
	public void fromClass(Class<?> clazz, MessageProperties messageProperties) {
		fromType(clazz, messageProperties);
	}

	public void fromType(Object objectToConvert, MessageProperties messageProperties) {
		addHeader(messageProperties, CLASS_ID_FIELD_NAME, objectToConvert.getClass());

		if (objectToConvert instanceof Collection && !((Collection<?>) objectToConvert).isEmpty()) {
			Object firstElement = ((Collection<?>) objectToConvert).iterator().next();
			addHeader(messageProperties, CONTENT_CLASS_ID_FIELD_NAME, firstElement != null ? firstElement.getClass() : Object.class);
		}

		if (objectToConvert instanceof Map && !((Map<?, ?>) objectToConvert).isEmpty()) {
			Object firstValue = ((Map<?, ?>) objectToConvert).values().iterator().next();
			addHeader(messageProperties, CONTENT_CLASS_ID_FIELD_NAME, firstValue != null ? firstValue.getClass() : Object.class);
			Object firstKey = ((Map<?, ?>) objectToConvert).keySet().iterator().next();
			addHeader(messageProperties, KEY_CLASS_ID_FIELD_NAME, firstKey != null ? firstKey.getClass() : Object.class);
		}
	}

	@Override
	public Class<?> toClass(MessageProperties messageProperties) {
		return (Class<?>) toType(messageProperties);
	}

	public Type toType(MessageProperties messageProperties) {
		String classTypeId = retrieveHeader(messageProperties, CLASS_ID_FIELD_NAME);
		String contentClassTypeId = retrieveHeader(messageProperties, CONTENT_CLASS_ID_FIELD_NAME);
		String keyClassTypeId = retrieveHeader(messageProperties, KEY_CLASS_ID_FIELD_NAME);
		if (classTypeId == null) {
			return null;
		}

		Type classType = getClassIdType(classTypeId);
		Type contentClassType = contentClassTypeId != null ? getClassIdType(contentClassTypeId) : null;
		Type keyClassType = keyClassTypeId != null ? getClassIdType(keyClassTypeId) : null;

		if (keyClassType != null) {
			return jsonObjectMapper.constructMapType((Class<? extends Map>) classType, (Class<?>) keyClassType, (Class<?>) contentClassType);
		}

		if (contentClassType != null) {
			return jsonObjectMapper.constructCollectionType((Class<? extends Collection>) classType, (Class<?>) contentClassType);
		}

		return classType;
	}

	private void addHeader(MessageProperties properties, String headerName, Class<?> clazz) {
		if (classIdMapping.containsKey(clazz)) {
			properties.getHeaders().put(headerName, classIdMapping.get(clazz));
		} else {
			properties.getHeaders().put(headerName, clazz.getName());
		}
	}

	private String retrieveHeader(MessageProperties properties, String headerName) {
		Map<String, Object> headers = properties.getHeaders();
		Object classIdFieldNameValue = headers.get(headerName);
		if (classIdFieldNameValue != null) {
			return classIdFieldNameValue.toString();
		}
		return null;
	}

}
