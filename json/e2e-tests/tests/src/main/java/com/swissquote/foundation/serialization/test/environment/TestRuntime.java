package com.swissquote.foundation.serialization.test.environment;

import lombok.Getter;

public enum TestRuntime {

	INSTANCE("server");

	@Getter
	private EndToEndTestRuntime testRuntime;

	TestRuntime(String instanceId) {
		testRuntime = new EndToEndTestRuntime(instanceId);
	}
}
