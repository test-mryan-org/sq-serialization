package com.swissquote.foundation.gson;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Before;
import org.junit.Test;

import com.swissquote.foundation.serialization.json.support.jaxrs.JsonObjectMapperProvider;

public class GsonProviderTest {

	private JsonObjectMapperProvider writeProvider;

	private JsonObjectMapperProvider readProvider;

	@Before
	public void initTest() {
		writeProvider = new JsonObjectMapperProvider();
		readProvider = new JsonObjectMapperProvider();
	}

	@Test
	public void test() {
		MediaType.valueOf("application/json;charset=utf-8");
	}

	@Test
	public void standard_string() throws IOException {

		PipedInputStream inputStream = new PipedInputStream();
		PipedOutputStream outputStream = new PipedOutputStream(inputStream);
		MultivaluedMap<String, Object> httpHeaders = new MultivaluedHashMap<>();

		writeProvider.writeTo("nټrd", String.class, String.class, null, //
				MediaType.APPLICATION_JSON_TYPE, httpHeaders, outputStream);
		outputStream.close();
		String json = fetchJsonFromInputStream(inputStream);

		assertEquals(1, httpHeaders.size());
	}

	private String fetchJsonFromInputStream(InputStream stream) {
		java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
