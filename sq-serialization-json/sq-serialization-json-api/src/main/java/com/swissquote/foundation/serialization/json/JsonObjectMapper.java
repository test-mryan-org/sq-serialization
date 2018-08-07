package com.swissquote.foundation.serialization.json;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

/**
 * Copy of org.springframework.integration.support.json.JsonObjectMapper adapted for SQ
 */
public interface JsonObjectMapper<N, P> {

	String DATE_FORMAT_PATTERN = "yyyy'-'MM'-'dd'T'HH':'mm':'ssZ";

	Collection<Class<?>> supportedJsonTypes =
			Arrays.<Class<?>> asList(String.class, byte[].class, byte[].class, File.class, InputStream.class, Reader.class);

	String toJson(Object value) throws Exception;

	void toJson(Object value, Writer writer) throws Exception;

	N toJsonNode(Object value) throws Exception;

	<T> T fromJson(Object json, Class<T> valueType) throws Exception;

	<T> T fromJson(P parser, Type valueType) throws Exception;

}
