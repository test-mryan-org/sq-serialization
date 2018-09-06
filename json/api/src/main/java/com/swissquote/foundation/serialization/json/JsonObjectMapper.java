package com.swissquote.foundation.serialization.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Copy of org.springframework.integration.support.json.JsonObjectMapper adapted for SQ
 */
public interface JsonObjectMapper<N, P> {

	String toJson(Object value) throws IOException;

	void toJson(Object value, Writer writer) throws IOException;

	N toJsonNode(Object value) throws IOException;

	<T> T fromJson(Object json, Class<T> valueType) throws IOException;

	<T> T fromJson(Object json, Type valueType) throws IOException;

	<T> T fromParser(P parser, Type valueType) throws IOException;

	<T> T unwrap(Class<T> cls);

}
