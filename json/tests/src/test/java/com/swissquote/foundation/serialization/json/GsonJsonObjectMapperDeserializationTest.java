package com.swissquote.foundation.serialization.json;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class GsonJsonObjectMapperDeserializationTest {

	private GsonJsonObjectMapper jsonObjectMapper = new GsonJsonObjectMapper();

	@Test
	public void testToJavaUtilDate() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-java-util-date.json"));

		// WHEN
		jsonObjectMapper.fromJson(json, TestObjectJavaUtilDate.class);
	}

	@Test
	public void testToInstant() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-instant.json"));

		// WHEN
		jsonObjectMapper.fromJson(json, TestObjectInstant.class);
	}

	@Test
	public void testToLocalDate() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-local-date.json"));

		// WHEN
		jsonObjectMapper.fromJson(json, TestObjectLocalDate.class);
	}

	@Test
	public void testToLocalDateTime() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-local-date-time.json"));

		// WHEN
		jsonObjectMapper.fromJson(json, TestObjectLocalDateTime.class);
	}

	@Test
	public void testToLocalTime() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-local-time.json"));

		// WHEN
		jsonObjectMapper.fromJson(json, TestObjectLocalTime.class);
	}

	@Test
	public void testToZonedDateTime() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-zoned-date-time.json"));

		// WHEN
		jsonObjectMapper.fromJson(json, TestObjectZonedDateTime.class);
	}

	@Test
	public void testToNullableValue() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-instant_null.json"));

		// WHEN

		TestObjectInstant obj = jsonObjectMapper.fromJson(json, TestObjectInstant.class);
		Assert.assertNull(obj.getName());
		Assert.assertNull(obj.getCreationDate());
	}

}
