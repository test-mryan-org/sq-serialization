package com.swissquote.foundation.serialization.json;

import static com.swissquote.foundation.serialization.json.DateTestUtils.getJavaUtilDate;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getLocalDate;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getLocalDateTime;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getLocalTime;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getZonedDateTime;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.swissquote.foundation.serialization.json.model.TestObjectInstant;
import com.swissquote.foundation.serialization.json.model.TestObjectJavaUtilDate;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalDate;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalDateTime;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalTime;
import com.swissquote.foundation.serialization.json.model.TestObjectZonedDateTime;

public class GsonJacksonObjectMapperTest {

	private JacksonJsonObjectMapper jacksonObjectMapper = new JacksonJsonObjectMapper();
	private GsonJsonObjectMapper gsonObjectMapper = new GsonJsonObjectMapper();

	@Test
	public void testFromJavaUtilDate() throws Exception {
		TestObjectJavaUtilDate object = new TestObjectJavaUtilDate("object", singletonList(getJavaUtilDate()));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		TestObjectJavaUtilDate jacksonObject = jacksonObjectMapper.fromJson(gsonString, TestObjectJavaUtilDate.class);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.getCreationDates().get(0).compareTo(getJavaUtilDate()) == 0);

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		TestObjectJavaUtilDate gsonObject = gsonObjectMapper.fromJson(jacksonString, TestObjectJavaUtilDate.class);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.getCreationDates().get(0).compareTo(getJavaUtilDate()) == 0);
	}

	@Test
	public void testFromInstant() throws Exception {
		TestObjectInstant object = new TestObjectInstant("object", singletonList(getJavaUtilDate().toInstant()));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		TestObjectInstant jacksonObject = jacksonObjectMapper.fromJson(gsonString, TestObjectInstant.class);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.getCreationDates().get(0).compareTo(getJavaUtilDate().toInstant()) == 0);

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		TestObjectInstant gsonObject = gsonObjectMapper.fromJson(jacksonString, TestObjectInstant.class);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.getCreationDates().get(0).compareTo(getJavaUtilDate().toInstant()) == 0);
	}

	@Test
	public void testFromLocalDate() throws Exception {
		TestObjectLocalDate object = new TestObjectLocalDate("object", singletonList(getLocalDate()));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		TestObjectLocalDate jacksonObject = jacksonObjectMapper.fromJson(gsonString, TestObjectLocalDate.class);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.getCreationDates().get(0).compareTo(getLocalDate()) == 0);

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		TestObjectLocalDate gsonObject = gsonObjectMapper.fromJson(jacksonString, TestObjectLocalDate.class);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.getCreationDates().get(0).compareTo(getLocalDate()) == 0);
	}

	@Test
	public void testFromLocalDateTime() throws Exception {
		TestObjectLocalDateTime object = new TestObjectLocalDateTime("object", singletonList(getLocalDateTime()));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		TestObjectLocalDateTime jacksonObject = jacksonObjectMapper.fromJson(gsonString, TestObjectLocalDateTime.class);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.getCreationDates().get(0).compareTo(getLocalDateTime()) == 0);

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		TestObjectLocalDateTime gsonObject = gsonObjectMapper.fromJson(jacksonString, TestObjectLocalDateTime.class);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.getCreationDates().get(0).compareTo(getLocalDateTime()) == 0);
	}

	@Test
	public void testFromLocalTime() throws Exception {
		TestObjectLocalTime object = new TestObjectLocalTime("object", singletonList(getLocalTime()));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		TestObjectLocalTime jacksonObject = jacksonObjectMapper.fromJson(gsonString, TestObjectLocalTime.class);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.getCreationDates().get(0).compareTo(getLocalTime()) == 0);

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		TestObjectLocalTime gsonObject = gsonObjectMapper.fromJson(jacksonString, TestObjectLocalTime.class);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.getCreationDates().get(0).compareTo(getLocalTime()) == 0);
	}

	@Test
	public void testFromZonedDateTime() throws Exception {
		TestObjectZonedDateTime object = new TestObjectZonedDateTime("object", singletonList(getZonedDateTime()));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		TestObjectZonedDateTime jacksonObject = jacksonObjectMapper.fromJson(gsonString, TestObjectZonedDateTime.class);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.getCreationDates().get(0).compareTo(getZonedDateTime()) == 0);

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		TestObjectZonedDateTime gsonObject = gsonObjectMapper.fromJson(jacksonString, TestObjectZonedDateTime.class);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.getCreationDates().get(0).compareTo(getZonedDateTime()) == 0);
	}

	@Test
	public void testFromException() throws Exception {
		Exception exception = new Exception("client-exception", new Exception(new Exception("root")));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(exception);

		// Deserialization with Jackson
		Exception jacksonObject = jacksonObjectMapper.fromJson(gsonString, Exception.class);
		assertNotNull(jacksonObject);

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(exception);

		// Deserialization with Gson
		Exception gsonObject = gsonObjectMapper.fromJson(jacksonString, Exception.class);
		assertNotNull(gsonObject);
	}

}
