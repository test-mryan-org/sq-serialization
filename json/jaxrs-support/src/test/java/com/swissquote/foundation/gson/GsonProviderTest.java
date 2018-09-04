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

import com.swissquote.foundation.serialization.json.support.jaxrs.GsonProvider;

public class GsonProviderTest {

	private GsonProvider writeGsonProvider;

	private GsonProvider readGsonProvider;

	@Before
	public void initTest() {
		writeGsonProvider = new GsonProvider();
		readGsonProvider = new GsonProvider();
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

		writeGsonProvider.writeTo("nټrd", String.class, String.class, null, //
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
