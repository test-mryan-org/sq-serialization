package com.swissquote.treasury.itests.environment;

import lombok.Getter;

public enum TestRuntime {

	INSTANCE("server");

	@Getter
	private EndToEndTestRuntime testRuntime;

	TestRuntime(String instanceId) {
		testRuntime = new EndToEndTestRuntime(instanceId);
	}
}
