package com.swissquote.foundation.serialization.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import com.swissquote.foundation.serialization.api.v1.entities.ComplexData;
import com.swissquote.foundation.serialization.test.spring.RMQServiceActivator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RMQSpringIntegrationSimpleTest extends AbstractTest {

	@Resource(name = "internal-outbound-channel")
	private MessageChannel outbound;

	@Test
	public void sendComplex() throws InterruptedException {
		ComplexData complexData = new ComplexData("data");
		// send with transformer and converter
		outbound.send(MessageBuilder.withPayload(complexData).build());

		List<ComplexData> resultComplexData = RMQServiceActivator.waitForComplexData();
		assertTrue(resultComplexData.size() == 2);
		assertThat(resultComplexData.get(0), is(complexData));
		assertThat(resultComplexData.get(1), is(complexData));
	}

	@Test
	public void sendMap() throws InterruptedException {
		Map<ComplexData, String> map = new HashMap<>();
		map.put(new ComplexData("data"), "default");
		// send with transformer and converter
		outbound.send(MessageBuilder.withPayload(map).build());

		List<Map> resultMap = RMQServiceActivator.waitForMap();
		assertTrue(resultMap.size() == 2);
		assertThat(resultMap.get(0), is(map));
		assertThat(resultMap.get(1), is(map));
	}

	@Test
	public void sendCollection() throws InterruptedException {
		List<ComplexData> list = new ArrayList<>();
		list.add(new ComplexData("data"));
		// send with transformer and converter
		outbound.send(MessageBuilder.withPayload(list).build());

		List<Collection> resultCollection = RMQServiceActivator.waitForCollection();
		assertTrue(resultCollection.size() == 2);
		assertThat(resultCollection.get(0), is(list));
		assertThat(resultCollection.get(1), is(list));
	}

}
