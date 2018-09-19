package com.swissquote.foundation.serialization.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.swissquote.foundation.serialization.api.v1.entities.ComplexData;
import com.swissquote.foundation.serialization.api.v1.entities.ComplexValue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SOASimpleTest extends AbstractTest {

	@Test
	public void soa_getString() {
		// url to test
		// http://sq-serialization-json-e2e-tests-soa-soa.docker
		//     /sq-serialization-json-e2e-tests-soa-soa/api/sq-serialization-e2e-tests/example/string
		String string = exampleResource().getString();
		assertThat(string, is("string"));
	}

	@Test
	public void soa_getComplexValue() {
		ComplexValue complexValue = exampleResource().getComplexValue();
		assertThat(complexValue, is(new ComplexValue("value")));
	}

	@Test
	public void soa_getComplexData() {
		ComplexData complexData = exampleResource().getComplexData();
		assertThat(complexData, is(new ComplexData("data")));
	}

	@Test
	public void soa_getMapComplexValueAsValue() {
		Map<String, ComplexValue> map = new HashMap<>();
		map.put("default", new ComplexValue("value"));

		Map<String, ComplexValue> mapComplexValue = exampleResource().getMapComplexValueAsValue();
		assertThat(mapComplexValue, is(map));
	}

	@Test
	public void soa_getMapComplexValueAsKey() {
		Map<ComplexValue, String> map = new HashMap<>();
		map.put(new ComplexValue("value"), "default");

		Map<ComplexValue, String> mapComplexValue = exampleResource().getMapComplexValueAsKey();
		assertThat(mapComplexValue, is(map));
	}

	@Test
	public void soa_getMapComplexDataAsValue() {
		Map<String, ComplexData> map = new HashMap<>();
		map.put("default", new ComplexData("data"));

		Map<String, ComplexData> mapComplexData = exampleResource().getMapComplexDataAsValue();
		assertThat(mapComplexData, is(map));
	}

	@Test
	public void soa_getMapComplexDataAsKey() {
		Map<ComplexData, String> map = new HashMap<>();
		map.put(new ComplexData("data"), "default");

		Map<ComplexData, String> mapComplexData = exampleResource().getMapComplexDataAsKey();
		assertThat(mapComplexData, is(map));
	}

}
