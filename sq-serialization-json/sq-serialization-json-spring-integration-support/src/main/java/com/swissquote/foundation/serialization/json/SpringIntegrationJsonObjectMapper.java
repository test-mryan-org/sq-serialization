package com.swissquote.foundation.serialization.json;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.springframework.integration.mapping.support.JsonHeaders;
import org.springframework.util.ClassUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringIntegrationJsonObjectMapper implements JsonObjectMapper<Map<String, Object>, Object> {

	private volatile ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
	private static final Collection<Class<?>> supportedJsonTypes =
			Arrays.<Class<?>> asList(String.class, byte[].class, byte[].class, File.class, InputStream.class, Reader.class);

	private final Gson gson;

	public SpringIntegrationJsonObjectMapper() {
		this(new GsonBuilder().create());
	}

	public SpringIntegrationJsonObjectMapper(Gson gson) {
		this.gson = gson;
	}

	@Override
	public String toJson(Object value) throws Exception {
		return gson.toJson(value);
	}

	@Override
	public void toJson(Object value, Writer writer) throws Exception {
		gson.toJson(value, writer);
	}

	@Override
	public <T> T fromJson(Object json, Class<T> valueType) throws Exception {

		if (json instanceof String) {
			return gson.fromJson((String) json, valueType);
		} else if (json instanceof byte[]) {
			return gson.fromJson(new InputStreamReader(new ByteArrayInputStream((byte[]) json)), valueType);
		} else if (json instanceof char[]) {
			return gson.fromJson(new String((char[]) json), valueType);
		} else if (json instanceof File) {
			try (Reader reader = new FileReader((File) json)) {
				return gson.fromJson(reader, valueType);
			}
		} else if (json instanceof InputStream) {
			return gson.fromJson(new InputStreamReader((InputStream) json), valueType);
		} else if (json instanceof Reader) {
			return gson.fromJson((Reader) json, valueType);
		} else {
			throw new IllegalArgumentException("'json' argument must be an instance of: " + supportedJsonTypes
					+ " , but gotten: " + json.getClass());
		}
	}

	@Override
	public <T> T fromJson(Object json, Map<String, Object> javaTypes) throws Exception {
		Class<?> classType = this.createJavaType(javaTypes, JsonHeaders.TYPE_ID);
		Class<?> contentClassType = this.createJavaType(javaTypes, JsonHeaders.CONTENT_TYPE_ID);
		Class<?> keyClassType = this.createJavaType(javaTypes, JsonHeaders.KEY_TYPE_ID);
		TypeToken<?> classTypeToken = TypeToken.get(classType);

		gson.fromJson((String) json, classTypeToken.getType());

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
