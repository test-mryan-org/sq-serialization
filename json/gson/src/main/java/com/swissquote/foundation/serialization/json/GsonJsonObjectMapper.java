package com.swissquote.foundation.serialization.json;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import java.util.Date;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.swissquote.foundation.serialization.gson.ThrowableFieldsExclusionStrategy;
import com.swissquote.foundation.serialization.gson.date.DateAdapter;
import com.swissquote.foundation.serialization.gson.date.InstantAdapter;
import com.swissquote.foundation.serialization.gson.date.LocalDateAdapter;
import com.swissquote.foundation.serialization.gson.date.LocalDateTimeAdapter;
import com.swissquote.foundation.serialization.gson.date.LocalTimeAdapter;
import com.swissquote.foundation.serialization.gson.date.ZonedDateTimeAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GsonJsonObjectMapper implements JsonObjectMapper<JsonElement, Object> {

	private static final String SERIALIZATION_DATE_FORMAT_PATTERN = "yyyy'-'MM'-'dd'T'HH':'mm':'ssZ";
	private static final String DESERIALIZATION_DATE_FORMAT_PATTERN = "yyyy'-'MM'-'dd'T'HH':'mm':'ssX";

	private static final Collection<Class<?>> supportedGsonJsonTypes =
			Arrays.asList(String.class, byte[].class, char[].class, File.class, InputStream.class, Reader.class);

	private final GsonBuilder builder;
	private Gson gson;

	public GsonJsonObjectMapper(GsonBuilder builder) {
		this.builder = builder;
		gson = builder.create();
	}

	public GsonJsonObjectMapper() {
		this(standardGsonBuilder());

	}

	/**
	 * SQ pre-defined GsonBuilder object
	 */
	public static GsonBuilder standardGsonBuilder() {
		return new GsonBuilder()
				.setDateFormat(SERIALIZATION_DATE_FORMAT_PATTERN)
				.registerTypeAdapter(Date.class, new DateAdapter(DESERIALIZATION_DATE_FORMAT_PATTERN))
				.registerTypeAdapter(Instant.class, new InstantAdapter())
				.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
				.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
				.setExclusionStrategies(new ThrowableFieldsExclusionStrategy())
				.enableComplexMapKeySerialization()
				.serializeSpecialFloatingPointValues();
	}

	public GsonJsonObjectMapper registerTypeAdapter(Type type, Object adapter) {
		synchronized (builder) {
			builder.registerTypeAdapter(type, adapter);
			gson = builder.create();
		}
		return this;
	}

	@Override
	public String toJson(Object value) {
		return gson.toJson(value);
	}

	@Override
	public void toJson(Object value, Writer writer) {
		gson.toJson(value, writer);
	}

	@Override
	public JsonElement toJsonNode(final Object value) {
		return gson.toJsonTree(value);
	}

	@Override
	public <T> T fromJson(Object json, Type typeOfT) throws IOException {
		if (json instanceof String) {
			return gson.fromJson((String) json, typeOfT);
		} else if (json instanceof byte[]) {
			try (ByteArrayInputStream bytes = new ByteArrayInputStream((byte[]) json);
					InputStreamReader reader = new InputStreamReader(bytes)) {
				return gson.fromJson(reader, typeOfT);
			}
		} else if (json instanceof char[]) {
			return gson.fromJson(new String((char[]) json), typeOfT);
		} else if (json instanceof File) {
			try (Reader reader = new FileReader((File) json)) {
				return gson.fromJson(reader, typeOfT);
			}
		} else if (json instanceof InputStream) {
			try (InputStreamReader reader = new InputStreamReader((InputStream) json)) {
				return gson.fromJson(reader, typeOfT);
			}
		} else if (json instanceof Reader) {
			return gson.fromJson((Reader) json, typeOfT);
		} else {
			throw new IllegalArgumentException("'json' argument must be an instance of: " + supportedGsonJsonTypes
					+ " , but gotten: " + json.getClass());
		}
	}

	@Override
	public <T> T fromJson(Object json, Class<T> valueType) throws IOException {
		if (json instanceof String) {
			return gson.fromJson((String) json, valueType);
		} else if (json instanceof byte[]) {
			try (ByteArrayInputStream bytes = new ByteArrayInputStream((byte[]) json);
					InputStreamReader reader = new InputStreamReader(bytes)) {
				return gson.fromJson(reader, valueType);
			}
		} else if (json instanceof char[]) {
			return gson.fromJson(new String((char[]) json), valueType);
		} else if (json instanceof File) {
			try (Reader reader = new FileReader((File) json)) {
				return gson.fromJson(reader, valueType);
			}
		} else if (json instanceof InputStream) {
			try (InputStreamReader reader = new InputStreamReader((InputStream) json)) {
				return gson.fromJson(reader, valueType);
			}
		} else if (json instanceof Reader) {
			return gson.fromJson((Reader) json, valueType);
		} else {
			throw new IllegalArgumentException("'json' argument must be an instance of: " + supportedGsonJsonTypes
					+ " , but gotten: " + json.getClass());
		}
	}

	@Override
	public <T> T fromParser(Object parser, Type valueType) {
		throw new UnsupportedOperationException("SQ Gson doesn't support JSON reader parser abstraction");
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
