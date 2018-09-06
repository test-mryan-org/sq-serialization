package com.swissquote.foundation.serialization.json;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class JacksonJsonObjectMapperDeserializationTest {

	private JacksonJsonObjectMapper jsonObjectMapper = new JacksonJsonObjectMapper();

	@Test
	public void testToJavaUtilDate() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-java-util-date.json"));

		// WHEN
		TestObjectJavaUtilDate obj = jsonObjectMapper.fromJson(json, TestObjectJavaUtilDate.class);
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
		TestObjectLocalDate obj = jsonObjectMapper.fromJson(json, TestObjectLocalDate.class);
		Assert.assertNotNull(obj);
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
		TestObjectLocalTime obj = jsonObjectMapper.fromJson(json, TestObjectLocalTime.class);
		Assert.assertNotNull(obj);
	}

	@Test
	public void testToZonedDateTime() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-zoned-date-time.json"));

		// WHEN
		TestObjectZonedDateTime obj = jsonObjectMapper.fromJson(json, TestObjectZonedDateTime.class);
	}

	@Test
	public void testNoDefaultConstructor() throws Exception {
		// GIVEN
		jsonObjectMapper.getObjectMapper().addMixIn(TestValueInstant.class, TestValueInstantMixin.class);
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-instant.json"));

		// WHEN
		jsonObjectMapper.fromJson(json, TestValueInstant.class);
	}

	@Test
	public void testToNullableValue() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-instant_null.json"));

		// WHEN
		TestObjectInstant obj = jsonObjectMapper.fromJson(json, TestObjectInstant.class);
		Assert.assertNull(obj.getName());
		Assert.assertNull(obj.getCreationDates());
	}

}
