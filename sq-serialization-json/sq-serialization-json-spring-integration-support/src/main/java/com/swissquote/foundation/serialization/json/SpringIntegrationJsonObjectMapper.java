package com.swissquote.foundation.serialization.json;

import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import org.springframework.integration.mapping.support.JsonHeaders;
import org.springframework.integration.support.json.JsonObjectMapperAdapter;
import org.springframework.util.ClassUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unchecked")
public class SpringIntegrationJsonObjectMapper<N, P> extends JsonObjectMapperAdapter<N, P> {

	private volatile ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

	private JsonObjectMapper<N, P> jsonObjectMapper;

	public SpringIntegrationJsonObjectMapper() {
		ServiceLoader<JsonObjectMapper> service = ServiceLoader.load(JsonObjectMapper.class);
		Iterator<JsonObjectMapper> iterator = service.iterator();

		if (!iterator.hasNext()) {
			throw new NullPointerException("No implementation of " + JsonObjectMapper.class.getName() + "found in META-INF");
		}

		jsonObjectMapper = iterator.next();

		if (iterator.hasNext()) {
			throw new IllegalArgumentException("More than one implementation of " + JsonObjectMapper.class.getName() + "has been provided");
		}
	}

	public SpringIntegrationJsonObjectMapper(JsonObjectMapper<N, P> jsonObjectMapper) {
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