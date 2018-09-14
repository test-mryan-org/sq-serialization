package com.swissquote.foundation.serialization.test.environment;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class E2ERule implements TestRule {

	private EndToEndTestRuntime runtime;
	private TestEnvironment environment;

	@Override
	public Statement apply(Statement base, Description description) {
		return new RunRuleStatement(base);
	}

	@RequiredArgsConstructor
	private class RunRuleStatement extends Statement {

		private final Statement next;

		@Override
		public void evaluate() throws Throwable {
			// Before
			runtime = TestRuntime.INSTANCE.getTestRuntime();
			runtime.getLogEvents().clear();
			runtime.getTestEnvironment().reset();
			environment = runtime.getTestEnvironment();
			// Push the action
			next.evaluate();
		}
	}

}