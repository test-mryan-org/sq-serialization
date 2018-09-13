package com.swissquote.foundation.serialization.json.support.spring.amqp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.core.ParameterizedTypeReference;

import com.swissquote.foundation.serialization.json.spi.GsonJsonSerializationProvider;
import com.swissquote.foundation.serialization.json.spi.JacksonJsonSerializationProvider;

@SuppressWarnings("Duplicates")
public class RoundSQJsonMessageConverterTest {

	private SQJsonMessageConverter gsonMessageConverter = new SQJsonMessageConverter(new GsonJsonSerializationProvider());
	private SQJsonMessageConverter jacksonMessageConverter = new SQJsonMessageConverter(new JacksonJsonSerializationProvider());

	@Test
	public void roundObjectTest() {
		Complex complex = new Complex("nom", 2);

		Message gsonMessage = gsonMessageConverter.toMessage(complex, new MessageProperties());
		Message jacksonMessage = jacksonMessageConverter.toMessage(complex, new MessageProperties());

		Complex fromGsonMessage = (Complex) jacksonMessageConverter.fromMessage(gsonMessage);
		Complex fromJacksonMessage = (Complex) gsonMessageConverter.fromMessage(jacksonMessage);

		assertThat(fromGsonMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.instanceOf(Complex.class)
		));
		assertEquals("nom", fromGsonMessage.getName());
		assertEquals(2, fromGsonMessage.getCount());

		assertThat(fromJacksonMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.instanceOf(Complex.class)
		));
		assertEquals("nom", fromJacksonMessage.getName());
		assertEquals(2, fromJacksonMessage.getCount());
	}

	@Test
	public void roundEmptyMapTest() {
		Map<String, Complex> map = new HashMap<>();

		Message gsonMessage = gsonMessageConverter.toMessage(map, new MessageProperties());
		Message jacksonMessage = jacksonMessageConverter.toMessage(map, new MessageProperties());

		Map<String, Complex> fromGsonMessage = (Map<String, Complex>) jacksonMessageConverter.fromMessage(gsonMessage);
		Map<String, Complex> fromJacksonMessage = (Map<String, Complex>) gsonMessageConverter.fromMessage(jacksonMessage);

		assertTrue(fromGsonMessage.isEmpty());
		assertTrue(fromJacksonMessage.isEmpty());
	}

	@Test
	public void roundMapTest() {
		Complex complex = new Complex("nom", 2);
		Map<String, Complex> map = new HashMap<>();
		map.put("default", complex);

		Message gsonMessage = gsonMessageConverter.toMessage(map, new MessageProperties());
		Message jacksonMessage = jacksonMessageConverter.toMessage(map, new MessageProperties());

		Map<String, Complex> fromGsonMessage = (Map<String, Complex>) jacksonMessageConverter.fromMessage(gsonMessage);
		Map<String, Complex> fromJacksonMessage = (Map<String, Complex>) gsonMessageConverter.fromMessage(jacksonMessage);

		assertThat(fromGsonMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry("default", complex)
		));
		assertEquals("nom", fromGsonMessage.get("default").getName());
		assertEquals(2, fromGsonMessage.get("default").getCount());

		assertThat(fromJacksonMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry("default", complex)
		));
		assertEquals("nom", fromJacksonMessage.get("default").getName());
		assertEquals(2, fromJacksonMessage.get("default").getCount());
	}

	@Test
	public void roundComplexKeyTest() {
		Complex complex = new Complex("nom", 2);
		Map<Complex, String> map = new HashMap<>();
		map.put(complex, "default");

		Message gsonMessage = gsonMessageConverter.toMessage(map, new MessageProperties());
		Message jacksonMessage = jacksonMessageConverter.toMessage(map, new MessageProperties());

		Map<Complex, String> fromGsonMessage = (Map<Complex, String>) jacksonMessageConverter.fromMessage(gsonMessage);
		Map<Complex, String> fromJacksonMessage = (Map<Complex, String>) gsonMessageConverter.fromMessage(jacksonMessage);

		assertThat(fromGsonMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry(complex, "default")
		));
		complex = fromGsonMessage.keySet().iterator().next();
		assertEquals("nom", complex.getName());
		assertEquals(2, complex.getCount());

		assertThat(fromJacksonMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry(complex, "default")
		));
		complex = fromJacksonMessage.keySet().iterator().next();
		assertEquals("nom", complex.getName());
		assertEquals(2, complex.getCount());
	}

	@Test
	public void roundComplexKeyWithTypeGsonTest() {
		Complex complex = new Complex("nom", 2);
		Map<Complex, String> map = new HashMap<>();
		map.put(complex, "default");

		// We set type only for gson message converter
		gsonMessageConverter.setType(new ParameterizedTypeReference<Map<Complex, String>>() {
		}.getType());

		Message gsonMessage = gsonMessageConverter.toMessage(map, new MessageProperties());
		Message jacksonMessage = jacksonMessageConverter.toMessage(map, new MessageProperties());

		Map<Complex, String> fromGsonMessage = (Map<Complex, String>) jacksonMessageConverter.fromMessage(gsonMessage);
		Map<Complex, String> fromJacksonMessage = (Map<Complex, String>) gsonMessageConverter.fromMessage(jacksonMessage);

		assertThat(fromGsonMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry(complex, "default")
		));
		complex = fromGsonMessage.keySet().iterator().next();
		assertEquals("nom", complex.getName());
		assertEquals(2, complex.getCount());

		assertThat(fromJacksonMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry(complex, "default")
		));
		complex = fromJacksonMessage.keySet().iterator().next();
		assertEquals("nom", complex.getName());
		assertEquals(2, complex.getCount());
	}

	@Test
	public void roundComplexKeyWithTypeJacksonTest() {
		Complex complex = new Complex("nom", 2);
		Map<Complex, String> map = new HashMap<>();
		map.put(complex, "default");

		// We set type only for jackson message converter
		jacksonMessageConverter.setType(new ParameterizedTypeReference<Map<Complex, String>>() {
		}.getType());

		Message gsonMessage = gsonMessageConverter.toMessage(map, new MessageProperties());
		Message jacksonMessage = jacksonMessageConverter.toMessage(map, new MessageProperties());

		Map<Complex, String> fromGsonMessage = (Map<Complex, String>) jacksonMessageConverter.fromMessage(gsonMessage);
		Map<Complex, String> fromJacksonMessage = (Map<Complex, String>) gsonMessageConverter.fromMessage(jacksonMessage);

		assertThat(fromGsonMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry(complex, "default")
		));
		complex = fromGsonMessage.keySet().iterator().next();
		assertEquals("nom", complex.getName());
		assertEquals(2, complex.getCount());

		assertThat(fromJacksonMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry(complex, "default")
		));
		complex = fromJacksonMessage.keySet().iterator().next();
		assertEquals("nom", complex.getName());
		assertEquals(2, complex.getCount());
	}

}

