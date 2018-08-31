package com.swissquote.foundation.serialization.json.support.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * http://stackoverflow.com/questions/25311472/jersey-json-and-stringmessageprovider-clash-application-json-methods-generate
 */
@Provider
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public class StringGsonProvider implements MessageBodyReader<String>, MessageBodyWriter<String> {

	private GsonProvider delegate;

	public StringGsonProvider() {
		delegate = new GsonProvider();
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return delegate.isReadable(type, genericType, annotations, mediaType);
	}

	@Override
	public String readFrom(Class<String> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		return (String) delegate.readFrom(Object.class, genericType, annotations, mediaType, httpHeaders, entityStream);
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return delegate.isWriteable(type, genericType, annotations, mediaType);
	}

	@Override
	public long getSize(String s, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return delegate.getSize(s, type, genericType, annotations, mediaType);
	}

	@Override
	public void writeTo(String s, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		delegate.writeTo(s, type, genericType, annotations, mediaType, httpHeaders, entityStream);
	}
}
