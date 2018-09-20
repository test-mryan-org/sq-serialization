package com.swissquote.foundation.serialization.test;

import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.awaitility.Duration;
import com.swissquote.foundation.serialization.api.v1.resources.ExampleResource;
import com.swissquote.foundation.serialization.test.environment.E2ERule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-integration-amqp.xml"})
public abstract class AbstractTest {

	static final Duration TIMEOUT = new Duration(30, TimeUnit.SECONDS);

	@Rule
	public E2ERule e2eRule = new E2ERule();

	ExampleResource exampleResource() {
		return e2eRule.getEnvironment().getExampleResource();
	}

}
