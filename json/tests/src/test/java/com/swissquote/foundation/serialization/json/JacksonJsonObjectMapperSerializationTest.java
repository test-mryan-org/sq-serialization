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
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissquote.foundation.serialization.json.model.TestObjectInstant;
import com.swissquote.foundation.serialization.json.model.TestObjectJavaUtilDate;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalDate;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalDateTime;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalTime;
import com.swissquote.foundation.serialization.json.model.TestObjectPoint;
import com.swissquote.foundation.serialization.json.model.TestObjectZonedDateTime;

import lombok.RequiredArgsConstructor;

public class JacksonJsonObjectMapperSerializationTest {

	private JsonObjectMapper jsonObjectMapper = new JacksonJsonObjectMapper();

	@Test
	public void testFromJavaUtilDate() throws Exception {
		// GIVEN
		TestObjectJavaUtilDate object = new TestObjectJavaUtilDate("object", singletonList(getJavaUtilDate()));
		TestObjectJavaUtilDate object2 = new TestObjectJavaUtilDate("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDates[0]", equalTo(getJavaUtilDateString()));
		with(SQJson2).assertNotDefined("creationDates");
		with(originalJson).assertThat("creationDates[0]", not(getJavaUtilDateString()));
	}

	@Test
	public void testFromInstant() throws Exception {
		// GIVEN
		TestObjectInstant object = new TestObjectInstant("object", singletonList(getJavaUtilDate().toInstant()));
		TestObjectInstant object2 = new TestObjectInstant("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDates[0]", equalTo(getInstantString()));
		with(SQJson2).assertNotDefined("creationDates");
		with(originalJson).assertThat("creationDates[0]", not(getInstantString()));
	}

	@Test
	public void testFromLocalDate() throws Exception {
		// GIVEN
		TestObjectLocalDate object = new TestObjectLocalDate("object", singletonList(getLocalDate()));
		TestObjectLocalDate object2 = new TestObjectLocalDate("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDates[0]", equalTo(getLocalDateString()));
		with(SQJson2).assertNotDefined("creationDates");
		with(originalJson).assertThat("creationDates[0]", not(getLocalDateString()));
	}

	@Test
	public void testFromLocalDateTime() throws Exception {
		// GIVEN
		TestObjectLocalDateTime object = new TestObjectLocalDateTime("object", singletonList(getLocalDateTime()));
		TestObjectLocalDateTime object2 = new TestObjectLocalDateTime("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDates[0]", equalTo(getLocalDateTimeString()));
		with(SQJson2).assertNotDefined("creationDates");
		with(originalJson).assertThat("creationDates[0]", not(getLocalDateTimeString()));
	}

	@Test
	public void testFromLocalTime() throws Exception {
		// GIVEN
		TestObjectLocalTime object = new TestObjectLocalTime("object", singletonList(getLocalTime()));
		TestObjectLocalTime object2 = new TestObjectLocalTime("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THEN
		with(SQJson).assertThat("creationDates[0]", equalTo(getLocalTimeString()));
		with(SQJson2).assertNotDefined("creationDates");
		with(originalJson).assertThat("creationDates[0]", not(getLocalTimeString()));
	}

	@Test
	public void testFromZonedDateTime() throws Exception {
		// GIVEN
		TestObjectZonedDateTime object = new TestObjectZonedDateTime("object", singletonList(getZonedDateTime()));
		TestObjectZonedDateTime object2 = new TestObjectZonedDateTime("object", null);

		// WHEN
		String SQJson = jsonObjectMapper.toJson(object);
		String SQJson2 = jsonObjectMapper.toJson(object2);
		String originalJson = new ObjectMapper().writeValueAsString(object);

		// THENObjectMapper
		with(SQJson).assertThat("creationDates[0]", equalTo(getZonedDateTimeString()));
		with(SQJson2).assertNotDefined("creationDates");
		with(originalJson).assertThat("creationDates[0]", not(getZonedDateTimeString()));
	}

	@Test
	public void testFromComplexMap() throws Exception {
		// GIVEN
		Map<TestObjectPoint, String> points = new HashMap<>();
		points.put(new TestObjectPoint(5, 2), "p1");
		points.put(new TestObjectPoint(4, 15), "p2");
		String expectedJson = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-point.json"));

		// WHEN
		String SQJson = jsonObjectMapper.toJson(points);
		String originalJson = new ObjectMapper().writeValueAsString(points);

		// THEN
		assertEquals(expectedJson, SQJson);
		assertNotEquals(expectedJson, originalJson);
	}

	@Test
	public void testFromException() throws Exception {

		// GIVEN
		Exception exception = new Exception("client-exception", new Exception(new Exception("root")));
		exception.addSuppressed(new Exception("suppressed"));

		// WHEN
		String SQJson = jsonObjectMapper.toJson(exception);
		String originalJson = new ObjectMapper().writeValueAsString(exception);

		// THEN
		with(SQJson).assertThat("detailMessage", equalTo("client-exception"));
		with(SQJson).assertNotDefined("suppressedExceptions");
		with(SQJson).assertNotDefined("stackTrace");
		with(originalJson).assertNotNull("suppressed");
		with(originalJson).assertNotNull("stackTrace");

		// AND WHEN
		String json = jsonObjectMapper.toJson(new WithExclusionField("test", "test2"));

		// THEN
		with(json).assertThat("stackTrace", equalTo("test"));
		with(json).assertThat("suppressedExceptions", equalTo("test2"));
		with(json).assertNotDefined("name");
	}

	@RequiredArgsConstructor
	public static class WithExclusionField {

		private final String stackTrace;
		private final String suppressedExceptions;

		public String getName() {
			return stackTrace;
		}
	}

}