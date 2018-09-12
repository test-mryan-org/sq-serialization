package com.swissquote.foundation.serialization.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

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

	Type constructCollectionType(Class<? extends Collection> collectionType, Class<?> contentClassType);

	Type constructMapType(Class<? extends Map> mapType, Class<?> keyClassType, Class<?> contentClassType);

	<T> T unwrap(Class<T> cls);

}
