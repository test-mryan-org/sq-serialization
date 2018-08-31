package com.swissquote.foundation.serialization.json;

import static com.jayway.jsonassert.JsonAssert.with;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getInstantString;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getJavaUtilDate;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getJavaUtilDateString;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getLocalDate;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getLocalDateString;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getLocalDateTime;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getLocalDateTimeString;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getLocalTime;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getLocalTimeString;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getZonedDateTime;
import static com.swissquote.foundation.serialization.json.DateTestUtils.getZonedDateTimeString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissquote.foundation.serialization.json.spi.JacksonJsonObjectMapper;
import com.swissquote.foundation.serialization.json.spi.JsonObjectMapper;

import lombok.RequiredArgsConstructor;

public class JacksonJsonObjectMapperSerializationTest {

	private JsonObjectMapper jsonObjectMapper = new JacksonJsonObjectMapper();

	@Test
	public void testFromJavaUtilDate() throws Exception {
		// GIVEN
		TestObjectJavaUtilDate object = new TestObjectJavaUtilDate("object", getJavaUtilDate());
		TestObjectJavaUtilDate object2 = new TestObjectJavaUtilDate("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDate", equalTo(getJavaUtilDateString()));
		with(SQJson2).assertNotDefined("creationDate");
		with(originalJson).assertThat("creationDate", not(getJavaUtilDateString()));
	}

	@Test
	public void testFromInstant() throws Exception {
		// GIVEN
		TestObjectInstant object = new TestObjectInstant("object", getJavaUtilDate().toInstant());
		TestObjectInstant object2 = new TestObjectInstant("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDate", equalTo(getInstantString()));
		with(SQJson2).assertNotDefined("creationDate");
		with(originalJson).assertThat("creationDate", not(getInstantString()));
	}

	@Test
	public void testFromLocalDate() throws Exception {
		// GIVEN
		TestObjectLocalDate object = new TestObjectLocalDate("object", getLocalDate());
		TestObjectLocalDate object2 = new TestObjectLocalDate("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDate", equalTo(getLocalDateString()));
		with(SQJson2).assertNotDefined("creationDate");
		with(originalJson).assertThat("creationDate", not(getLocalDateString()));
	}

	@Test
	public void testFromLocalDateTime() throws Exception {
		// GIVEN
		TestObjectLocalDateTime object = new TestObjectLocalDateTime("object", getLocalDateTime());
		TestObjectLocalDateTime object2 = new TestObjectLocalDateTime("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDate", equalTo(getLocalDateTimeString()));
		with(SQJson2).assertNotDefined("creationDate");
		with(originalJson).assertThat("creationDate", not(getLocalDateTimeString()));
	}

	@Test
	public void testFromLocalTime() throws Exception {
		// GIVEN
		TestObjectLocalTime object = new TestObjectLocalTime("object", getLocalTime());
		TestObjectLocalTime object2 = new TestObjectLocalTime("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDate", equalTo(getLocalTimeString()));
		with(SQJson2).assertNotDefined("creationDate");
		with(originalJson).assertThat("creationDate", not(getLocalTimeString()));
	}

	@Test
	public void testFromZonedDateTime() throws Exception {
		// GIVEN
		TestObjectZonedDateTime object = new TestObjectZonedDateTime("object", getZonedDateTime());
		TestObjectZonedDateTime object2 = new TestObjectZonedDateTime("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDate", equalTo(getZonedDateTimeString()));
		with(SQJson2).assertNotDefined("creationDate");
		with(originalJson).assertThat("creationDate", not(getZonedDateTimeString()));
	}

	@Test
	public void testFromException() throws Exception {

		// GIVEN
		Exception exception = new Exception("client-exception", new Exception(new Exception("root")));
		exception.addSuppressed(new Exception("suppressed"));

		// WHEN
		String SQJson = jsonObjectMapper.toJson(exception);
		String originalJson = new ObjectMapper().writeValueAsString(exception);
		//		String s = jsonObjectMapper.toJson(new Self(new Self(new Self())));

		// THEN
		with(SQJson).assertThat("detailMessage", equalTo("client-exception"));
		with(SQJson).assertNotDefined("suppressedExceptions");
		with(SQJson).assertNotDefined("stackTrace");
		with(originalJson).assertNotNull("suppressedExceptions");
		with(originalJson).assertNotNull("stackTrace");

		// AND WHEN
		String json = jsonObjectMapper.toJson(new WithExclusionField("test", "test2"));

		// THEN
		with(json).assertThat("stackTrace", equalTo("test"));
		with(json).assertThat("suppressedExceptions", equalTo("test2"));
		with(json).assertNotDefined("name");
	}

//	@RequiredArgsConstructor
//	public static class Self {
//		private final Self self;
//
//		public Self() {
//			self = this;
//		}
//	}

	@RequiredArgsConstructor
	public static class WithExclusionField {

		private final String stackTrace;
		private final String suppressedExceptions;

		public String getName() {
			return stackTrace;
		}
	}

}