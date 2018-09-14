package com.swissquote.treasury.itests.environment;

import com.swissquote.foundation.serialization.api.v1.resources.ExampleResource;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdeffaye on 06/04/18.
 */
@Slf4j
@Value
public class TestEnvironment {

	private final TransactionNotificationAmqpListener notificationAmqpListener = new TransactionNotificationAmqpListener();
	private final EndToEndTestRuntime runtime;
	private final ExampleResource exampleResource;

	TestEnvironment(EndToEndTestRuntime runtime) {
		this.runtime = runtime;
		this.exampleResource = getSoaResource(ExampleResource.class);
	}

	private <T> T getSoaResource(Class<T> resourceClass) {
		return runtime.getSqSoaResource(
				"sq-serialization-json-e2e-tests-soa-soa",
				"sq-serialization-e2e-tests",
				"dev",
				"dev",
				resourceClass,
				false);
	}

	public void reset() {
		log.info("Reset notifications");
		notificationAmqpListener.clear();
	}
}
