package com.swissquote.foundation.serialization.json;

import java.io.Writer;
import java.util.Map;

/**
 * Copy of org.springframework.integration.support.json.JsonObjectMapper adapted for SQ
 */
public interface JsonObjectMapper<N, P> {

	String toJson(Object value) throws Exception;

	void toJson(Object value, Writer writer) throws Exception;

	<T> T fromJson(Object json, Class<T> valueType) throws Exception;

	<T> T fromJson(Object json, Map<String, Object> javaTypes) throws Exception;

}
