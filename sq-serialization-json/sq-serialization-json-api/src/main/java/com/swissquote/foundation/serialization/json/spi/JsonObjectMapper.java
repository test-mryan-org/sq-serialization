package com.swissquote.foundation.serialization.json.spi;

import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Copy of org.springframework.integration.support.json.JsonObjectMapper adapted for SQ
 */
public interface JsonObjectMapper<N, P> {

	String DATE_FORMAT_PATTERN = "yyyy'-'MM'-'dd'T'HH':'mm':'ssZ";

	String toJson(Object value) throws Exception;

	void toJson(Object value, Writer writer) throws Exception;

	N toJsonNode(Object value) throws Exception;

	<T> T fromJson(Object json, Class<T> valueType) throws Exception;

	<T> T fromJson(P parser, Type valueType) throws Exception;

}
