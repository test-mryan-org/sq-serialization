package com.swissquote.foundation.serialization.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Getter;

public class JacksonJsonObjectMapper implements JsonObjectMapper<JsonNode, JsonParser> {

	private static final Collection<Class<?>> supportedJacksonJsonTypes =
			Arrays.asList(String.class, byte[].class, File.class, URL.class, InputStream.class, Reader.class);

	@Getter
	private final ObjectMapper objectMapper;

	public JacksonJsonObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * SQ pre-defined object mapper
	 */
	public JacksonJsonObjectMapper() {
		this(standardObjectMapper()
				.findAndRegisterModules());
	}

	public static ObjectMapper standardObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper()
				.setDateFormat(new SimpleDateFormat(DATE_FORMAT_PATTERN))
				.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.setVisibility(PropertyAccessor.ALL, Visibility.NONE)
				.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
				// new need to explicitly register the module
				// https://github.com/FasterXML/jackson-modules-java8/blob/jackson-modules-java8-2.9.6/README.md#registering-modules
				.registerModule(new JavaTimeModule());


		return objectMapper;
	}

	@Override
	public String toJson(Object value) throws IOException {
		return objectMapper.writeValueAsString(value);
	}

	@Override
	public void toJson(Object value, Writer writer) throws IOException {
		objectMapper.writeValue(writer, value);
	}

	@Override
	public JsonNode toJsonNode(Object value) {
		return objectMapper.valueToTree(value);
	}

	@Override
	public <T> T fromJson(Object json, Class<T> valueType) throws IOException {
		if (json instanceof String) {
			return this.objectMapper.readValue((String) json, valueType);
		} else if (json instanceof byte[]) {
			return this.objectMapper.readValue((byte[]) json, valueType);
		} else if (json instanceof File) {
			return this.objectMapper.readValue((File) json, valueType);
		} else if (json instanceof URL) {
			return this.objectMapper.readValue((URL) json, valueType);
		} else if (json instanceof InputStream) {
			return this.objectMapper.readValue((InputStream) json, valueType);
		} else if (json instanceof Reader) {
			return this.objectMapper.readValue((Reader) json, valueType);
		} else {
			throw new IllegalArgumentException("'json' argument must be an instance of: " + supportedJacksonJsonTypes
					+ " , but gotten: " + json.getClass());
		}
	}

	@Override
	public <T> T fromJson(Object json, Type typeOfT) throws IOException {
		JavaType javaType = objectMapper.constructType(typeOfT);
		if (json instanceof String) {
			return this.objectMapper.readValue((String) json, javaType);
		} else if (json instanceof byte[]) {
			return this.objectMapper.readValue((byte[]) json, javaType);
		} else if (json instanceof File) {
			return this.objectMapper.readValue((File) json, javaType);
		} else if (json instanceof URL) {
			return this.objectMapper.readValue((URL) json, javaType);
		} else if (json instanceof InputStream) {
			return this.objectMapper.readValue((InputStream) json, javaType);
		} else if (json instanceof Reader) {
			return this.objectMapper.readValue((Reader) json, javaType);
		} else {
			throw new IllegalArgumentException("'json' argument must be an instance of: " + supportedJacksonJsonTypes
					+ " , but gotten: " + json.getClass());
		}
	}

	@Override
	public <T> T fromParser(JsonParser parser, Type valueType) throws IOException {
		return objectMapper.readValue(parser, objectMapper.constructType(valueType));
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		Objects.requireNonNull(cls, "argument cannot be null");
		if (cls.isAssignableFrom(getClass())) {
			return cls.cast(this);
		}
		throw new IllegalArgumentException("Unwapping to " + cls
				+ " is not a supported by this implementation");
	}
}
