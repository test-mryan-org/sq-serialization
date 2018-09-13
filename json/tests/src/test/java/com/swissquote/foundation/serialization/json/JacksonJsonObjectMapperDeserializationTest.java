package com.swissquote.foundation.serialization.json;

import static com.swissquote.foundation.serialization.json.values.TestValues.STRING_HELLO;
import static com.swissquote.foundation.serialization.json.values.TestValues.STRING_KEY_1;
import static com.swissquote.foundation.serialization.json.values.TestValues.STRING_KEY_2;
import static com.swissquote.foundation.serialization.json.values.TestValues.STRING_WORLD;
import static com.swissquote.foundation.serialization.json.values.TestValues.TF_HELLO;
import static com.swissquote.foundation.serialization.json.values.TestValues.TF_WORLD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.reflect.TypeToken;
import com.swissquote.foundation.serialization.json.model.TestContainsComplicatedMap;
import com.swissquote.foundation.serialization.json.model.TestContainsComplicatedMapMixin;
import com.swissquote.foundation.serialization.json.model.TestContainsEasyMap;
import com.swissquote.foundation.serialization.json.model.TestObjectInstant;
import com.swissquote.foundation.serialization.json.model.TestObjectJavaUtilDate;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalDate;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalDateTime;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalTime;
import com.swissquote.foundation.serialization.json.model.TestObjectPoint;
import com.swissquote.foundation.serialization.json.model.TestObjectZonedDateTime;
import com.swissquote.foundation.serialization.json.model.TestThreeFields;
import com.swissquote.foundation.serialization.json.model.TestThreeFieldsMixin;
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

	@Test(expected = InvalidDefinitionException.class)
	public void testNoDefaultConstructorMissingMixin() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-point.json"));

		// WHEN
		TestObjectPoint obj = jsonObjectMapper.fromJson(json, TestObjectPoint.class);

		// EXPECT InvalidDefinitionException
	}

	@Test
	public void testToNullableValue() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-instant_null.json"));

		// WHEN
		TestObjectInstant obj = jsonObjectMapper.fromJson(json, TestObjectInstant.class);
		assertNull(obj.getName());
		assertNull(obj.getCreationDates());
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

	@Test(expected = MismatchedInputException.class)
	public void testFromEasyMapArrays() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-easyMapArray.json"));

		// WHEN
		TestContainsEasyMap easyMap = jsonObjectMapper.fromJson(json, TestContainsEasyMap.class);

		// EXPECTED exception (expected = MismatchedInputException.class)
	}

	@Test
	public void testFromEasyMapArraysComplex() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-complicatedMapArray.json"));

		// WHEN
		TestContainsComplicatedMap complicatedMap = jsonObjectMapper.fromJson(json, TestContainsComplicatedMap.class);

		//THEN
		assertNotNull(complicatedMap);
		Map<TestThreeFields, String> insideMap = complicatedMap.getComplicatedMap();
		assertNotNull(insideMap);
		assertEquals(insideMap.size(), 2);
		assertEquals(insideMap.get(TF_HELLO), STRING_HELLO);
		assertEquals(insideMap.get(TF_WORLD), STRING_WORLD);

	}

	@Test
	public void testFromMapComplexKeyAsBadlyFormattedArray() throws Exception {
		// GIVEN
		String json = IOUtils.toString(
				this.getClass().getClassLoader().getResourceAsStream("json/object-complicatedMapArray-badlyFormatted.json"));
		Type type = new TypeToken<Map<TestThreeFields, String>>() {
		}.getType();

		// WHEN
		TestContainsComplicatedMap complicatedMap = jsonObjectMapper.fromJson(json, TestContainsComplicatedMap.class);

		//THEN
		assertNotNull(complicatedMap);
		Map<TestThreeFields, String> insideMap = complicatedMap.getComplicatedMap();
		assertNotNull(insideMap);
		assertEquals(insideMap.size(), 2);
		assertEquals(insideMap.get(TF_HELLO), STRING_HELLO);
		assertEquals(insideMap.get(TF_WORLD), STRING_WORLD);

	}

	@Test
	public void testFromEasyMapArraysComplexWithMixin() throws Exception {
		// GIVEN
		jsonObjectMapper.getObjectMapper().addMixIn(TestContainsComplicatedMap.class, TestContainsComplicatedMapMixin.class);
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-complicatedMapArray.json"));

		// WHEN
		TestContainsComplicatedMap complicatedMap = jsonObjectMapper.fromJson(json, TestContainsComplicatedMap.class);

		//THEN
		Map<TestThreeFields, String> insideMap = complicatedMap.getComplicatedMap();
		assertNotNull(insideMap);
		assertEquals(insideMap.size(), 2);
		assertEquals(insideMap.get(TF_HELLO), STRING_HELLO);
		assertEquals(insideMap.get(TF_WORLD), STRING_WORLD);

	}

	@Test
	public void testFromEasyMapBraces() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-easyMap.json"));

		// WHEN
		TestContainsEasyMap easyMap = jsonObjectMapper.fromJson(json, TestContainsEasyMap.class);

		//THEN
		assertNotNull(easyMap.getEasyMap());
		assert (easyMap.getEasyMap().containsKey(STRING_KEY_1));
		assertEquals(easyMap.getEasyMap().get(STRING_KEY_1), STRING_HELLO);
		assert (easyMap.getEasyMap().containsKey(STRING_KEY_2));
		assertEquals(easyMap.getEasyMap().get(STRING_KEY_2), STRING_WORLD);

	}

	@Test
	public void testFromMapComplexKeyAsArray() throws Exception {
		// GIVEN
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-mapComplexKeyAsArray.json"));
		Type type = new TypeToken<Map<TestThreeFields, String>>() {
		}.getType();

		// WHEN
		Map<TestThreeFields, String> map = jsonObjectMapper.fromJson(json, type);

		//THEN
		assertNotNull(map);
		assertEquals(map.size(), 2);
		assertEquals(map.get(TF_HELLO), STRING_HELLO);
		assertEquals(map.get(TF_WORLD), STRING_WORLD);
	}

	@Test
	public void testFromMapComplexKeyComplexValueAsArray() throws Exception {
		// GIVEN
		String json = IOUtils.toString(
				this.getClass().getClassLoader().getResourceAsStream("json/object-mapComplexKeyComplexValueAsArray.json"));
		Type type = new TypeToken<Map<TestThreeFields, TestThreeFields>>() {
		}.getType();

		// WHEN
		Map<TestThreeFields, TestThreeFields> map = jsonObjectMapper.fromJson(json, type);

		//THEN
		assertNotNull(map);
		assertEquals(map.size(), 2);
		assertEquals(map.get(TF_HELLO), TF_WORLD);
		assertEquals(map.get(TF_WORLD), TF_HELLO);

	}

	@Test
	public void testFromMapSimpleKeyComplexValueWithBraces() throws Exception {
		// GIVEN
		String json = IOUtils.toString(
				this.getClass().getClassLoader().getResourceAsStream("json/object-mapSimpleKeyComplexValueWithBraces.json"));
		Type type = new TypeToken<Map<String, TestThreeFields>>() {
		}.getType();

		// WHEN
		Map<String, TestThreeFields> map = jsonObjectMapper.fromJson(json, type);

		//THEN
		assertNotNull(map);
		assertEquals(map.size(), 2);
		assertEquals(map.get(STRING_HELLO), TF_HELLO);
		assertEquals(map.get(STRING_WORLD), TF_WORLD);

	}

	@Test
	public void testFromMapEmptyWithBracesToSimpleKey() throws Exception {
		// GIVEN
		String json = IOUtils.toString(
				this.getClass().getClassLoader().getResourceAsStream("json/object-mapEmptyWithBraces.json"));
		Type type = new TypeToken<Map<String, TestThreeFields>>() {
		}.getType();

		// WHEN
		Map<String, TestThreeFields> map = jsonObjectMapper.fromJson(json, type);

		//THEN
		assertNotNull(map);
		assertEquals(map.size(), 0);

	}

	@Test
	public void testFromMapEmptyWithBracesToComplexKey() throws Exception {
		// GIVEN
		String json = IOUtils.toString(
				this.getClass().getClassLoader().getResourceAsStream("json/object-mapEmptyWithBraces.json"));
		Type type = new TypeToken<Map<TestThreeFields, TestThreeFields>>() {
		}.getType();

		// WHEN
		Map<TestThreeFields, TestThreeFields> map = jsonObjectMapper.fromJson(json, type);

		//THEN
		assertNotNull(map);
		assertEquals(map.size(), 0);
	}

	@Test(expected = MismatchedInputException.class)
	public void testFromMapEmptyAsArraysToSimpleKey() throws Exception {
		// GIVEN
		String json = IOUtils.toString(
				this.getClass().getClassLoader().getResourceAsStream("json/object-mapEmptyAsArray.json"));
		Type type = new TypeToken<Map<String, TestThreeFields>>() {
		}.getType();

		// WHEN
		Map<String, TestThreeFields> map = jsonObjectMapper.fromJson(json, type);

		// EXPECT (expected = MismatchedInputException.class)

	}

	@Test
	public void testFromMapEmptyAsArraysToComplexKey() throws Exception {
		// GIVEN
		String json = IOUtils.toString(
				this.getClass().getClassLoader().getResourceAsStream("json/object-mapEmptyAsArray.json"));
		Type type = new TypeToken<Map<TestThreeFields, TestThreeFields>>() {
		}.getType();

		// WHEN
		Map<TestThreeFields, TestThreeFields> map = jsonObjectMapper.fromJson(json, type);

		//THEN
		assertNotNull(map);
		assertEquals(map.size(), 0);
	}

	@Test
	public void testFromMapComplexKeyAsArrayWithMixin() throws Exception {
		// GIVEN
		jsonObjectMapper.getObjectMapper().addMixIn(TestThreeFields.class, TestThreeFieldsMixin.class);
		String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("json/object-mapComplexKeyAsArray.json"));
		Type type = TypeToken.getParameterized(Map.class, TestThreeFields.class, String.class).getType();

		// WHEN
		Map<TestThreeFields, String> map = jsonObjectMapper.fromJson(json, type);

		//THEN
		assertNotNull(map);
		assertEquals(map.size(), 2);
		assertEquals(map.get(TF_HELLO), STRING_HELLO);
		assertEquals(map.get(TF_WORLD), STRING_WORLD);

	}

}
