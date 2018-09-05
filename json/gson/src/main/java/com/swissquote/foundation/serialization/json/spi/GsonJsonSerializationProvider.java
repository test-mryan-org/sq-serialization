package com.swissquote.foundation.serialization.json.spi;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.WeakHashMap;

import com.swissquote.foundation.serialization.json.GsonJsonObjectMapper;
import com.swissquote.foundation.serialization.json.JsonObjectMapper;

public class GsonJsonSerializationProvider implements JsonSerializationProvider {

	private static final Properties EMPTY = new Properties();

	private final Map<Properties, JsonObjectMapper<?, ?>> objectMappers = new WeakHashMap<>();

	@Override
	public <N, P> JsonObjectMapper<N, P> getJsonObjectMapper() {
		return getJsonObjectMapper(EMPTY);
	}

	@Override
	public <N, P> JsonObjectMapper<N, P> getJsonObjectMapper(Properties props) {
		return (JsonObjectMapper<N, P>) getObjectMapper(props);
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

	private JsonObjectMapper<?, ?> getObjectMapper(Properties properties) {
		synchronized (objectMappers) {
			return objectMappers.computeIfAbsent(properties, key -> buildObjectMapper(key));
		}
	}

	private GsonJsonObjectMapper buildObjectMapper(Properties properties) {
		// Currently only the standard object is return. In the future we may want to convert
		// properties parameter -> GsonBuilder
		return new GsonJsonObjectMapper(GsonJsonObjectMapper.standardGsonBuilder());
	}
}
