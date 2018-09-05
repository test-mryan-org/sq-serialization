package com.swissquote.foundation.serialization.json.spi;

import java.util.Properties;

import com.swissquote.foundation.serialization.json.JsonObjectMapper;
import com.swissquote.foundation.serialization.json.JsonSerialization;

/**
 * SPI Interface
 *
 * @see JsonSerialization for bootstrapping to load the provider
 */
public interface JsonSerializationProvider {

	/**
	 * Returns a default {@link JsonObjectMapper} up and ready with all sq standard
	 */
	<N, P> JsonObjectMapper<N, P> getJsonObjectMapper();

	/**
	 * Returns an instance of {@link JsonObjectMapper} configured by given properties
	 * Properties are implementation dependent
	 */
	<N, P> JsonObjectMapper<N, P> getJsonObjectMapper(Properties props);

	/**
	 * Provides a standard mechanism to access the underlying concrete serialization
	 * implementation
	 */
	<T> T unwrap(Class<T> clazz);
}
