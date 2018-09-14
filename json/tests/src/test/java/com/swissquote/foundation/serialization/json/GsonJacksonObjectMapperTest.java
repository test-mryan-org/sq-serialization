package com.swissquote.foundation.serialization.json;

import static com.swissquote.foundation.serialization.json.TestUtils.getJavaUtilDate;
import static com.swissquote.foundation.serialization.json.TestUtils.getLocalDate;
import static com.swissquote.foundation.serialization.json.TestUtils.getLocalDateTime;
import static com.swissquote.foundation.serialization.json.TestUtils.getLocalTime;
import static com.swissquote.foundation.serialization.json.TestUtils.getZonedDateTime;
import static com.swissquote.foundation.serialization.json.TestUtils.mapOf;
import static com.swissquote.foundation.serialization.json.values.TestValues.STRING_HELLO;
import static com.swissquote.foundation.serialization.json.values.TestValues.STRING_WORLD;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.google.gson.reflect.TypeToken;
import com.swissquote.foundation.serialization.json.model.TestObjectInstant;
import com.swissquote.foundation.serialization.json.model.TestObjectJavaUtilDate;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalDate;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalDateTime;
import com.swissquote.foundation.serialization.json.model.TestObjectLocalTime;
import com.swissquote.foundation.serialization.json.model.TestObjectPoint;
import com.swissquote.foundation.serialization.json.model.TestObjectZonedDateTime;
import com.swissquote.foundation.serialization.json.model.TestThreeFields;

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
	public void testSimpleMap() throws Exception {
		Map<String, Integer> object = mapOf("k1", 1, "k2", 2);
		Type typeOfMap = TypeToken.getParameterized(Map.class, String.class, Integer.class).getType();

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		Map<String, Integer> gsonObject = gsonObjectMapper.fromJson(jacksonString, typeOfMap);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.size() == 2);
		assertThat(gsonObject, allOf(
				hasEntry("k1", 1),
				hasEntry("k2", 2)
		));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		Map<String, Integer> jacksonObject = jacksonObjectMapper.fromJson(gsonString, typeOfMap);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.size() == 2);
		assertThat(jacksonObject, allOf(
				hasEntry("k1", 1),
				hasEntry("k2", 2)
		));

	}

	@Test
	public void testSimpleIntegerMap() throws Exception {
		Map<Integer, Integer> object = mapOf(1, 1, 2, 2);
		Type typeOfMap = TypeToken.getParameterized(Map.class, Integer.class, Integer.class).getType();

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		Map<Integer, Integer> gsonObject = gsonObjectMapper.fromJson(jacksonString, typeOfMap);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.size() == 2);
		assertThat(gsonObject, allOf(
				hasEntry(1, 1),
				hasEntry(2, 2)
		));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		Map<Integer, Integer> jacksonObject = jacksonObjectMapper.fromJson(gsonString, typeOfMap);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.size() == 2);
		assertThat(jacksonObject, allOf(
				hasEntry(1, 1),
				hasEntry(2, 2)
		));
	}

	@Test
	public void testSimpleMultipleMap() throws Exception {
		Map<String, Map<String, Boolean>> object = mapOf(
				"k1",
				mapOf("K1", true, "K2", true),
				"k2",
				mapOf("K1", false, "K2", false));

		Type typeOfMap =
				TypeToken.getParameterized(Map.class, String.class, TypeToken.getParameterized(Map.class, String.class, Boolean.class).getType())
						.getType();

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		Map<String, Map<String, Boolean>> gsonObject = gsonObjectMapper.fromJson(jacksonString, typeOfMap);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.size() == 2);
		assertThat(gsonObject, allOf(
				hasEntry(is("k1"), allOf(
						hasEntry("K1", true),
						hasEntry("K2", true)
				)),
				hasEntry(is("k2"), allOf(
						hasEntry("K1", false),
						hasEntry("K2", false)
				))
		));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		Map<String, Map<String, Boolean>> jacksonObject = jacksonObjectMapper.fromJson(gsonString, typeOfMap);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.size() == 2);
		assertThat(jacksonObject, allOf(
				hasEntry(is("k1"), allOf(
						hasEntry("K1", true),
						hasEntry("K2", true)
				)),
				hasEntry(is("k2"), allOf(
						hasEntry("K1", false),
						hasEntry("K2", false)
				))
		));
	}

	@Test(expected = InvalidDefinitionException.class)
	public void testComplexMap() throws Exception {
		TestObjectPoint k1 = new TestObjectPoint(1, 2);
		TestObjectPoint k2 = new TestObjectPoint(3, 4);
		Map<TestObjectPoint, Integer> object = mapOf(k1, 1, k2, 2);
		Type typeOfMap = TypeToken.getParameterized(Map.class, TestObjectPoint.class, Integer.class).getType();

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		Map<TestObjectPoint, Integer> gsonObject = gsonObjectMapper.fromJson(jacksonString, typeOfMap);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.size() == 2);
		assertThat(gsonObject, allOf(
				hasEntry(k1, 1),
				hasEntry(k2, 2)
		));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson -> Will fail because no Mixin was set for TestObjectPoint and it lacks a Creator
		Map<TestObjectPoint, Integer> jacksonObject = jacksonObjectMapper.fromJson(gsonString, typeOfMap);
	}

	@Test
	public void testComplexMapThreeFields() throws Exception {
		TestThreeFields k1 = new TestThreeFields("Foo", 42L, new BigDecimal("42"));
		TestThreeFields k2 = new TestThreeFields("Bar", 1337L, new BigDecimal("1337"));

		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);

		Map<TestThreeFields, Integer> object = mapOf(k1, v1, k2, v2);
		Type typeOfMap = TypeToken.getParameterized(Map.class, TestThreeFields.class, Integer.class).getType();

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		Map<TestThreeFields, Integer> gsonObject = gsonObjectMapper.fromJson(jacksonString, typeOfMap);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.size() == 2);
		assertThat(gsonObject, allOf(
				hasEntry(k1, v1),
				hasEntry(k2, v2)
		));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		Map<TestThreeFields, Integer> jacksonObject = jacksonObjectMapper.fromJson(gsonString, typeOfMap);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.size() == 2);
		assertThat(jacksonObject, allOf(
				hasEntry(k1, v1),
				hasEntry(k2, v2)
		));

	}

	@Test
	public void testMapSimpleKeyComplexValue() throws Exception {
		TestThreeFields val1 = new TestThreeFields("Foo", 42L, new BigDecimal("42"));
		TestThreeFields val2 = new TestThreeFields("Bar", 1337L, new BigDecimal("1337"));

		Map<String, TestThreeFields> object = mapOf(STRING_HELLO, val1, STRING_WORLD, val2);
		Type typeOfMap = TypeToken.getParameterized(Map.class, String.class, TestThreeFields.class).getType();

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		System.out.println(jacksonString);

		// Deserialization with Gson
		Map<String, TestThreeFields> gsonObject = gsonObjectMapper.fromJson(jacksonString, typeOfMap);
		gsonObject.entrySet().stream().forEach(System.out::println);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.size() == 2);
		assertThat(gsonObject, allOf(
				hasEntry(STRING_HELLO, val1),
				hasEntry(STRING_WORLD, val2)
		));

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);
		System.out.println(gsonString);

		// Deserialization with Jackson
		Map<String, TestThreeFields> jacksonObject = jacksonObjectMapper.fromJson(jacksonString, typeOfMap);
		jacksonObject.entrySet().stream().forEach(System.out::println);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.size() == 2);
		assertThat(jacksonObject, allOf(
				hasEntry(STRING_HELLO, val1),
				hasEntry(STRING_WORLD, val2)
		));

	}

	@Test
	public void testEmptyComplexMap() throws Exception {

		Map<TestThreeFields, Integer> object = new HashMap<>();
		Type typeOfMap = TypeToken.getParameterized(Map.class, TestThreeFields.class, Integer.class).getType();

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		Map<TestThreeFields, Integer> gsonObject = gsonObjectMapper.fromJson(jacksonString, typeOfMap);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.size() == 0);

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		Map<TestThreeFields, Integer> jacksonObject = jacksonObjectMapper.fromJson(gsonString, typeOfMap);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.size() == 0);

	}

	@Test
	public void testEmptySimpleKeyComplexValueMap() throws Exception {

		Map<Integer, TestThreeFields> object = new HashMap<>();
		Type typeOfMap = TypeToken.getParameterized(Map.class, Integer.class, TestThreeFields.class).getType();

		// Serialization with Jackson
		String jacksonString = jacksonObjectMapper.toJson(object);

		// Deserialization with Gson
		Map<Integer, TestThreeFields> gsonObject = gsonObjectMapper.fromJson(jacksonString, typeOfMap);
		assertNotNull(gsonObject);
		assertTrue(gsonObject.size() == 0);

		// Serialization with Gson
		String gsonString = gsonObjectMapper.toJson(object);

		// Deserialization with Jackson
		Map<Integer, TestThreeFields> jacksonObject = jacksonObjectMapper.fromJson(gsonString, typeOfMap);
		assertNotNull(jacksonObject);
		assertTrue(jacksonObject.size() == 0);

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
