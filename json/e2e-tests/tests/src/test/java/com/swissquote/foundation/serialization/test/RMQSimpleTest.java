package com.swissquote.foundation.serialization.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.messaging.MessageChannel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// This class is just an example
public class RMQSimpleTest extends AbstractTest {

	@Resource(name = "internal-outbound-channel")
	private MessageChannel outbound;

	@Test
	public void toImplement() {
		//		example how to send a message with spring integration
		//		outbound.send(MessageBuilder.withPayload(new ComplexData("data")).build());
	}

}
