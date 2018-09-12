package com.swissquote.foundation.serialization.json.support.spring.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.RequiredArgsConstructor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Context.class)
@RequiredArgsConstructor
public class IntegrationTest {

	@Autowired
	@Qualifier("inbound-channel")
	private MessageChannel inboundChannel;

	@Autowired
	private OutboundEndpoint outboundEndpoint;

	@Test
	public void test() {

		List<Event> list = new ArrayList<>();
		list.add(new Event(Arrays.asList(new Event.Value("value1"), new Event.Value("value2"))));

		inboundChannel.send(MessageBuilder.withPayload(list).build());

		Assert.assertThat(outboundEndpoint.getEvents(), Matchers.hasSize(1));
		Assert.assertThat(outboundEndpoint.getEvents().get(0), Matchers.is(list));

	}

}
