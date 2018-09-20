package com.swissquote.foundation.serialization.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swissquote.foundation.serialization.api.v1.entities.ComplexData;
import com.swissquote.foundation.serialization.test.spring.RMQMessageHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RMQSpringSimpleTest extends AbstractTest {

	private static final String EXCHANGE = "global.tx";

	private static AmqpTemplate amqpTemplate;

	@BeforeClass
	public static void init() {
		// in this case prefer BeforeClass rather than using Rule
		// because amqpTemplate is necessary only for this test
		amqpTemplate = (AmqpTemplate) new ClassPathXmlApplicationContext("spring-rabbit.xml")
				.getBean("rabbitTemplateConverter");
	}

	@Test
	public void sendComplex() throws InterruptedException {
		ComplexData complexData = new ComplexData("data");
		// send with converter
		amqpTemplate.convertAndSend(EXCHANGE, "common.spring.complex", complexData);

		List<ComplexData> resultComplexData = RMQMessageHandler.waitForComplexData();
		assertTrue("we should get 1 message from queue but got " + resultComplexData.size(),
				resultComplexData.size() == 1);
		assertThat(resultComplexData.get(0), is(complexData));
	}

	@Test
	public void sendMap() throws InterruptedException {
		Map<ComplexData, String> map = new HashMap<>();
		map.put(new ComplexData("data"), "default");
		// send with converter
		amqpTemplate.convertAndSend(EXCHANGE, "common.spring.map", map);

		List<Map> resultMap = RMQMessageHandler.waitForMap();
		assertTrue("we should get 1 message from queue but got " + resultMap.size(),
				resultMap.size() == 1);
		assertThat(resultMap.get(0), is(map));
	}

	@Test
	public void sendCollection() throws InterruptedException {
		List<ComplexData> list = new ArrayList<>();
		list.add(new ComplexData("data"));
		// send with converter
		amqpTemplate.convertAndSend(EXCHANGE, "common.spring.collection", list);

		List<Collection> resultCollection = RMQMessageHandler.waitForCollection();
		assertTrue("we should get 1 message from queue but got " + resultCollection.size(),
				resultCollection.size() == 1);
		assertThat(resultCollection.get(0), is(list));
	}

}
