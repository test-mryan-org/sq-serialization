package com.swissquote.foundation.serialization.test.environment;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdeffaye on 06/04/18.
 */
@Slf4j
public class EndToEndTestLifecycle extends RunListener {

	private EndToEndTestRuntime getTestRuntime() {
		return TestRuntime.INSTANCE.getTestRuntime();
	}

	@Override
	public void testRunStarted(Description description) {
		getTestRuntime().pull();

		log.info("Starting Sandbox...");
		getTestRuntime().start();
		getTestRuntime().waitUntilReady();
	}

	@Override
	public void testRunFinished(Result result) {
		if (isCleanEnabled()) {
			getTestRuntime().stop();
		}
	}

	@Override
	public void testStarted(Description description) {
		log.info("***");
		log.info("*** Running test {}", description.getMethodName());
		log.info("***");
	}

	@Override
	public void testFinished(Description description) {
		log.info("***");
		log.info("*** Test {} Finished", description.getMethodName());
		log.info("***");
	}

	private boolean isCleanEnabled() {
		String cleanValue = System.getProperty("e2e.clean");
		return cleanValue == null || Boolean.parseBoolean(cleanValue);
	}
}
