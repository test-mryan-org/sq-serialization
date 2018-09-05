package com.swissquote.foundation.serialization.json.support.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.swissquote.foundation.serialization.json.JsonObjectMapper;
import com.swissquote.foundation.serialization.json.JsonSerialization;

@Provider
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public class JsonObjectMapperProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
	private static final Charset UTF8 = Charset.forName("UTF-8");

	private final JsonObjectMapper<?, ?> objectMapper;

	public JsonObjectMapperProvider() {
		this.objectMapper = JsonSerialization.getSerializationProvider().getJsonObjectMapper();
	}

	public static final Charset getCharset(final MediaType m) {
		String name = m == null ? null : m.getParameters().get(MediaType.CHARSET_PARAMETER);
		return name == null ? UTF8 : Charset.forName(name);
	}

	@Override
	public long getSize(final Object t, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType
			mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return true;
	}

	@Override
	public void writeTo(final Object t, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType,
			final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
		Charset charset = getCharset(mediaType);
		OutputStreamWriter stream = new OutputStreamWriter(entityStream, charset);

		httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, mediaType.withCharset(charset.name()).toString());

		objectMapper.toJson(t, stream);

		stream.flush();
	}

	@Override
	public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return true;
	}

	@Override
	public Object readFrom(final Class<Object> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType,
			final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream) throws IOException, WebApplicationException {

		return objectMapper.fromJson(new InputStreamReader(entityStream, getCharset(mediaType)), genericType);
	}

}
