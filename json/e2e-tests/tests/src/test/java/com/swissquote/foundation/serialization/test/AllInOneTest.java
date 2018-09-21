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
import com.swissquote.foundation.serialization.test.spring.RMQMessageHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllInOneTest extends AbstractTest {

	/* in this class, integrate everything in a smart manner in order to test all components:
	 *
	 *		- mix gson-jackson with soa --> DONE
	 *			-- soa server uses gson (sq-serialization-json-e2e-tests-soa-soa)
	 *			-- soa client uses jackson (sq-serialization-json-e2e-tests-tests)
	 *
	 *		- mix gson-jackson with spring --> DONE
	 *			-- for object mappers used in spring-integration transformers (sq-serialization-json-tests)
	 *			-- for message converters used in spring-integration and spring-rabbit (sq-serialization-json-spring-amqp-support)
	 *
	 *		- mix object transformer and message converter --> TODO
	 *
	 *		- receive from soa (sent with gson, read with jackson), send by transformer with jackson, read by converter with gson --> TODO
	 */

	@Resource(name = "mix-outbound-channel")
	private MessageChannel mixOutbound;

	@Resource(name = "all-outbound-channel")
	private MessageChannel allOutbound;

	@Test
	public void mixTransformerConverter() throws InterruptedException {
		ComplexData complexData = new ComplexData("data");
		Map<ComplexData, String> map = new HashMap<>();
		map.put(new ComplexData("data"), "default");
		List<ComplexData> list = new ArrayList<>();
		list.add(new ComplexData("data"));

		// send with transformer
		mixOutbound.send(MessageBuilder.withPayload(complexData).build());
		mixOutbound.send(MessageBuilder.withPayload(map).build());
		mixOutbound.send(MessageBuilder.withPayload(list).build());

		// receive with converter
		List<ComplexData> resultComplexData = RMQMessageHandler.waitForComplexData();
		List<Map> resultMap = RMQMessageHandler.waitForMap();
		List<Collection> resultCollection = RMQMessageHandler.waitForCollection();

		assertTrue("we should get 1 message from queue but got " + resultComplexData.size(),
				resultComplexData.size() == 1);
		assertThat(resultComplexData.get(0), is(complexData));
		assertTrue("we should get 1 message from queue but got " + resultMap.size(),
				resultMap.size() == 1);
		assertThat(resultMap.get(0), is(map));
		assertTrue("we should get 1 message from queue but got " + resultCollection.size(),
				resultCollection.size() == 1);
		assertThat(resultCollection.get(0), is(list));
	}

	/**
	 * Too complicated to run this test because it needs for the dependence sq-serialization-json-gson
	 * and because of SPI, we cannot have it alongside sq-serialization-json-jackson in the pom
	 */
	public void allInOneTest() throws InterruptedException {
		Map<ComplexData, String> originalMap = new HashMap<>();
		originalMap.put(new ComplexData("data"), "default");

		// receive from soa (sent with gson, read with jackson)
		Map<ComplexData, String> soaMapComplexData = exampleResource().getMapComplexDataAsKey();
		assertThat(soaMapComplexData, is(originalMap));

		// send with gson transformer
		allOutbound.send(MessageBuilder.withPayload(soaMapComplexData).build());

		// receive with jackson converter
		List<Map> resultMapComplexData = RMQMessageHandler.waitForMap();
		assertTrue("we should get 1 message from queue but got " + resultMapComplexData.size(),
				resultMapComplexData.size() == 1);
		assertThat(resultMapComplexData.get(0), is(originalMap));
		assertThat(resultMapComplexData.get(0), is(soaMapComplexData));

	}

}
