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

import com.swissquote.foundation.serialization.json.spi.JacksonJsonSerializationProvider;

@SuppressWarnings("Duplicates")
public class JacksonSQJsonMessageConverterTest {

	private SQJsonMessageConverter messageConverter = new SQJsonMessageConverter(new JacksonJsonSerializationProvider());

	@Test
	public void roundObjectTest() {
		Complex complex = new Complex("nom", 2);

		Message message = messageConverter.toMessage(complex, new MessageProperties());

		Complex fromMessage = (Complex) messageConverter.fromMessage(message);

		assertThat(fromMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.instanceOf(Complex.class)
		));
		assertEquals("nom", fromMessage.getName());
		assertEquals(2, fromMessage.getCount());
	}

	@Test
	public void roundEmptyMapTest() {
		Map<String, Complex> map = new HashMap<>();

		Message message = messageConverter.toMessage(map, new MessageProperties());

		Map<String, Complex> fromMessage = (Map<String, Complex>) messageConverter.fromMessage(message);

		assertTrue(fromMessage.isEmpty());
	}

	@Test
	public void roundMapTest() {
		Complex complex = new Complex("nom", 2);
		Map<String, Complex> map = new HashMap<>();
		map.put("default", complex);

		Message message = messageConverter.toMessage(map, new MessageProperties());

		Map<String, Complex> fromMessage = (Map<String, Complex>) messageConverter.fromMessage(message);

		assertThat(fromMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry("default", complex)
		));
		assertEquals("nom", fromMessage.get("default").getName());
		assertEquals(2, fromMessage.get("default").getCount());
	}

	@Test
	public void roundMapWithTypeTest() {
		Complex complex = new Complex("nom", 2);
		Map<String, Complex> map = new HashMap<>();
		map.put("default", complex);

		messageConverter.setType(new ParameterizedTypeReference<Map<String, Complex>>() {
		}.getType());

		Message message = messageConverter.toMessage(map, new MessageProperties());

		Map<String, Complex> fromMessage = (Map<String, Complex>) messageConverter.fromMessage(message);

		assertThat(fromMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry("default", complex)
		));
		assertEquals("nom", fromMessage.get("default").getName());
		assertEquals(2, fromMessage.get("default").getCount());
	}

	@Test
	public void roundComplexKeyTest() {
		Complex complex = new Complex("nom", 2);
		Map<Complex, String> map = new HashMap<>();
		map.put(complex, "default");

		Message message = messageConverter.toMessage(map, new MessageProperties());

		Map<Complex, String> fromMessage = (Map<Complex, String>) messageConverter.fromMessage(message);

		assertThat(fromMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry(complex, "default")
		));
		complex = fromMessage.keySet().iterator().next();
		assertEquals("nom", complex.getName());
		assertEquals(2, complex.getCount());
	}

	@Test
	public void roundComplexKeyWithTypeTest() {
		Complex complex = new Complex("nom", 2);
		Map<Complex, String> map = new HashMap<>();
		map.put(complex, "default");

		messageConverter.setType(new ParameterizedTypeReference<Map<Complex, String>>() {
		}.getType());

		Message message = messageConverter.toMessage(map, new MessageProperties());

		Map<Complex, String> fromMessage = (Map<Complex, String>) messageConverter.fromMessage(message);

		assertThat(fromMessage, Matchers.allOf(
				Matchers.notNullValue(),
				Matchers.hasEntry(complex, "default")
		));
		complex = fromMessage.keySet().iterator().next();
		assertEquals("nom", complex.getName());
		assertEquals(2, complex.getCount());
	}

}

