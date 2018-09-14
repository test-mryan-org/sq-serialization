package com.swissquote.foundation.serialization.test;

import static com.jayway.awaitility.Awaitility.waitAtMost;

import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.awaitility.Duration;
import com.swissquote.foundation.serialization.api.v1.resources.ExampleResource;
import com.swissquote.foundation.serialization.test.environment.E2ERule;
import com.swissquote.foundation.serialization.test.environment.TransactionNotificationAmqpListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-unit-test.xml"})
public abstract class AbstractTest {

	static final Duration TIMEOUT = new Duration(30, TimeUnit.SECONDS);

	@Rule
	public E2ERule e2eRule = new E2ERule();


	ExampleResource exampleResource() {
		return e2eRule.getEnvironment().getExampleResource();
	}

	void verifyRmqNotificationReceived(Matcher<Iterable<Message>> matcher) {
		TransactionNotificationAmqpListener notificationAmqpListener = e2eRule.getEnvironment()
				.getNotificationAmqpListener();


		waitAtMost(TIMEOUT).until(notificationAmqpListener::getNotifications, matcher);
	}

}
