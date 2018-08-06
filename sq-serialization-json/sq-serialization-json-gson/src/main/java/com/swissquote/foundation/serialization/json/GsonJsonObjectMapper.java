package com.swissquote.foundation.serialization.json;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.swissquote.foundation.soa.gson.date.InstantAdapter;
import com.swissquote.foundation.soa.gson.date.LocalDateAdapter;
import com.swissquote.foundation.soa.gson.date.LocalDateTimeAdapter;
import com.swissquote.foundation.soa.gson.date.LocalTimeAdapter;
import com.swissquote.foundation.soa.gson.date.ZonedDateTimeAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GsonJsonObjectMapper implements JsonObjectMapper<JsonElement, Object> {

	private static final Collection<Class<?>> supportedJsonTypes =
			Arrays.<Class<?>> asList(String.class, byte[].class, byte[].class, File.class, InputStream.class, Reader.class);

	private static final String DATE_FORMAT_PATTERN = "yyyy'-'MM'-'dd'T'HH':'mm':'ssZ";

	private final GsonBuilder builder;
	private Gson gson;

	public GsonJsonObjectMapper() {
		builder = new GsonBuilder()
				.setDateFormat(DATE_FORMAT_PATTERN)
				.registerTypeAdapter(Instant.class, new InstantAdapter())
				.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
				.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
				.enableComplexMapKeySerialization()
				.serializeSpecialFloatingPointValues();
		gson = builder.create();
	}

	public void registerTypeAdapter(Type type, Object adapter) {
		builder.registerTypeAdapter(type, adapter);
		gson = builder.create();
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
	public JsonElement toJsonNode(final Object value) throws Exception {
		return gson.toJsonTree(value);
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
	public <T> T fromJson(Object parser, Type valueType) throws Exception {
		throw new UnsupportedOperationException("SQ Gson doesn't support JSON reader parser abstraction");
	}

}
