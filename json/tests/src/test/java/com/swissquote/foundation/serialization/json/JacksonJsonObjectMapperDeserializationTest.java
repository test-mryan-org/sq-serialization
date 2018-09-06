package com.swissquote.foundation.serialization.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.swissquote.foundation.serialization.json.model.TestObjectInstant;
import com.swissquote.foundation.serialization.json.model.TestObjectJavaUtilDate;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalDate;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalDateTime;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalTime;
import com.swissquote.foundation.serialization.json.model.TestObjectZonedDateTime;
import com.swissquote.foundation.serialization.json.model.TestValueInstant;
import com.swissquote.foundation.serialization.json.model.TestValueInstantMixin;

@SuppressWarnings("Duplicates")
public class JacksonJsonObjectMapperDeserializationTest {

	private JacksonJsonObjectMapper jsonObjectMapper = new JacksonJsonObjectMapper();

	@Test
	public void testToJavaUtilDate() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-java-util-date.json"));

		// WHEN
		TestObjectJavaUtilDate obj = jsonObjectMapper.fromJson(json, TestObjectJavaUtilDate.class);

		// THEN
		assertNotNull(obj);
		assertTrue(obj.getCreationDates().get(0).compareTo(obj.getCreationDates().get(1)) == 0);
		assertTrue(obj.getCreationDates().get(0).compareTo(obj.getCreationDates().get(2)) == 0);
		assertTrue(obj.getCreationDates().get(0).compareTo(obj.getCreationDates().get(3)) == 0);
		Calendar cal1 = Calendar.getInstance(), cal2 = Calendar.getInstance();
		cal1.setTime(obj.getCreationDates().get(0));
		cal2.setTime(obj.getCreationDates().get(4));
		assertEquals(cal1.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		assertEquals(cal1.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals(cal1.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	public void testToInstant() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-instant.json"));

		// WHEN
		TestObjectInstant obj = jsonObjectMapper.fromJson(json, TestObjectInstant.class);

		// THEN
		assertNotNull(obj);
	}

	@Test
	public void testToLocalDate() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-local-date.json"));

		// WHEN
		TestObjectLocalDate obj = jsonObjectMapper.fromJson(json, TestObjectLocalDate.class);

		// THEN
		assertNotNull(obj);
		Calendar cal1 = Calendar.getInstance(), cal2 = Calendar.getInstance();
		cal1.setTime(Date.from(obj.getCreationDates().get(0).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		cal2.setTime(Date.from(obj.getCreationDates().get(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		assertEquals(cal1.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		assertEquals(cal1.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals(cal1.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	public void testToLocalDateTime() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-local-date-time.json"));

		// WHEN
		TestObjectLocalDateTime obj = jsonObjectMapper.fromJson(json, TestObjectLocalDateTime.class);

		// THEN
		assertNotNull(obj);
	}

	@Test
	public void testToLocalTime() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-local-time.json"));

		// WHEN
		TestObjectLocalTime obj = jsonObjectMapper.fromJson(json, TestObjectLocalTime.class);

		// THEN
		assertNotNull(obj);
	}

	@Test
	public void testToZonedDateTime() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-zoned-date-time.json"));

		// WHEN
		TestObjectZonedDateTime obj = jsonObjectMapper.fromJson(json, TestObjectZonedDateTime.class);

		// THEN
		assertNotNull(obj);
		assertTrue(obj.getCreationDates().get(0).isEqual(obj.getCreationDates().get(1)));
		assertTrue(obj.getCreationDates().get(0).isEqual(obj.getCreationDates().get(2)));
		assertTrue(obj.getCreationDates().get(0).isEqual(obj.getCreationDates().get(3)));
	}

	@Test
	public void testNoDefaultConstructor() throws Exception {
		// GIVEN
		jsonObjectMapper.getObjectMapper().addMixIn(TestValueInstant.class, TestValueInstantMixin.class);
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-instant.json"));

		// WHEN
		TestValueInstant obj = jsonObjectMapper.fromJson(json, TestValueInstant.class);

		// THEN
		assertNotNull(obj);
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

	@Test
	public void testToException() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/exception.json"));

		// WHEN
		Exception obj = jsonObjectMapper.fromJson(json, Exception.class);

		// THEN
		assertNotNull(obj);
	}

}
