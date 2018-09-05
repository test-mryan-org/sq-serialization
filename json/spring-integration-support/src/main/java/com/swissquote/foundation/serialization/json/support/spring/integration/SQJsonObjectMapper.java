package com.swissquote.foundation.serialization.json.support.spring.integration;

import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.springframework.integration.mapping.support.JsonHeaders;
import org.springframework.integration.support.json.JsonObjectMapperAdapter;
import org.springframework.util.ClassUtils;

import com.swissquote.foundation.serialization.json.JsonObjectMapper;
import com.swissquote.foundation.serialization.json.JsonSerialization;
import com.swissquote.foundation.serialization.json.spi.JsonSerializationProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unchecked")
public class SQJsonObjectMapper<N, P> extends JsonObjectMapperAdapter<N, P> {

	private volatile ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

	private JsonObjectMapper<N, P> jsonObjectMapper;

	public SQJsonObjectMapper() {
		this(JsonSerialization.getSerializationProvider());
	}

	public SQJsonObjectMapper(JsonSerializationProvider provider) {
		this.jsonObjectMapper = provider.getJsonObjectMapper();
	}

	public SQJsonObjectMapper(JsonObjectMapper<N, P> jsonObjectMapper) {
		this.jsonObjectMapper = jsonObjectMapper;
	}

	@Override
	public String toJson(Object value) throws Exception {
		return jsonObjectMapper.toJson(value);
	}

	@Override
	public void toJson(Object value, Writer writer) throws Exception {
		jsonObjectMapper.toJson(value, writer);
	}

	@Override
	public N toJsonNode(Object value) throws Exception {
		return jsonObjectMapper.toJsonNode(value);
	}

	@Override
	public <T> T fromJson(Object json, Class<T> valueType) throws Exception {
		return jsonObjectMapper.fromJson(json, valueType);
	}

	@Override
	public <T> T fromJson(Object json, Map<String, Object> javaTypes) throws Exception {
		Class<?> classType = this.createJavaType(javaTypes, JsonHeaders.TYPE_ID);
		Class<?> contentClassType = this.createJavaType(javaTypes, JsonHeaders.CONTENT_TYPE_ID);
		Class<?> keyClassType = this.createJavaType(javaTypes, JsonHeaders.KEY_TYPE_ID);

		if (keyClassType != null) {
			log.warn("Gson doesn't support the Map 'key' conversion. Will be returned raw Map<String, Object>");
			return (T) fromJson(json, Map.class);
		}

		if (contentClassType != null) {
			log.warn("Gson doesn't support the Collection conversion. Will be returned raw Collection<Object>");
			return (T) fromJson(json, Collection.class);
		}

		return (T) fromJson(json, classType);
	}

	@Override
	public <T> T fromJson(P parser, Type valueType) throws Exception {
		return jsonObjectMapper.fromJson(parser, valueType);
	}

	private Class<?> createJavaType(Map<String, Object> javaTypes, String javaTypeKey) throws Exception {
		Object classValue = javaTypes.get(javaTypeKey);
		if (classValue instanceof Class<?>) {
			return (Class<?>) classValue;
		} else if (classValue != null) {
			return ClassUtils.forName(classValue.toString(), this.classLoader);
		} else {
			return null;
		}
	}
}