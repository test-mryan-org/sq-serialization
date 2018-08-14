package com.swissquote.foundation.serialization.json.spi;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		this(new ObjectMapper()
				.setDateFormat(new SimpleDateFormat(DATE_FORMAT_PATTERN))
				.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.findAndRegisterModules());
	}

	@Override
	public String toJson(Object value) throws Exception {
		return objectMapper.writeValueAsString(value);
	}

	@Override
	public void toJson(Object value, Writer writer) throws Exception {
		objectMapper.writeValue(writer, value);
	}

	@Override
	public JsonNode toJsonNode(Object value) throws Exception {
		return objectMapper.valueToTree(value);
	}

	@Override
	public <T> T fromJson(Object json, Class<T> valueType) throws Exception {
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
	public <T> T fromJson(JsonParser parser, Type valueType) throws Exception {
		return objectMapper.readValue(parser, objectMapper.constructType(valueType));
	}
}