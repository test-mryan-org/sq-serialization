package com.swissquote.treasury.itests.environment;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jetty.connector.JettyConnectorProvider;
import org.glassfish.jersey.logging.LoggingFeature;

import com.github.swissquote.carnotzet.core.Carnotzet;
import com.github.swissquote.carnotzet.core.CarnotzetConfig;
import com.github.swissquote.carnotzet.core.maven.CarnotzetModuleCoordinates;
import com.github.swissquote.carnotzet.core.runtime.api.Container;
import com.github.swissquote.carnotzet.core.runtime.api.ContainerOrchestrationRuntime;
import com.github.swissquote.carnotzet.core.runtime.api.PullPolicy;
import com.github.swissquote.carnotzet.core.runtime.log.LogEvents;
import com.github.swissquote.carnotzet.core.runtime.log.StdOutLogPrinter;
import com.github.swissquote.carnotzet.runtime.docker.compose.DockerComposeRuntime;
import com.swissquote.foundation.sandbox.core.SwissquoteCarnotzetConfig;
import com.swissquote.foundation.soa.client.SqSoaWebTargetFactory;
import com.swissquote.foundation.soa.client.WebProxyBuilder;
import com.swissquote.foundation.soa.client.config.ServiceConfig;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdeffaye on 06/04/18.
 */
@Slf4j
public class EndToEndTestRuntime {

	private static final int TWO_MINUTES = 2 * 60 * 1000;

	private final ContainerOrchestrationRuntime runtime;
	@Getter
	private final LogEvents logEvents = new LogEvents();

	private TestEnvironment testEnvironment;

	EndToEndTestRuntime(String instanceId) {
		CarnotzetConfig carnotzetConfig = SwissquoteCarnotzetConfig.builder()
				.topLevelModuleId(CarnotzetModuleCoordinates.fromPom(Paths.get("../sandbox/pom.xml")))
				.resourcesPath(Paths.get("../sandbox/target/carnotzet/" + instanceId).toAbsolutePath())
				.build();
		Carnotzet sandbox = new Carnotzet(carnotzetConfig);
		runtime = new DockerComposeRuntime(sandbox, "sq-serialization-json-e2e-" + instanceId);
		runtime.registerLogListener(logEvents);
		runtime.registerLogListener(new StdOutLogPrinter(sandbox, 0, true));
	}

	TestEnvironment getTestEnvironment() {
		if (testEnvironment == null) {
			testEnvironment = new TestEnvironment(this);
		}
		return testEnvironment;
	}

	private static ClientConfig buildClientConfig() {
		return new ClientConfig()
				// register FULL logger for debugging. Set Level.INFO to turn it on.
				.register(LoggingFeature.class)
				//				.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, Level.WARNING.getName())
				// register SIMPLE logger for normal usage (logs only outgoing requests lines, no headers, no responses)
				.register(SimpleClientLoggingFilter.class)
				// We disable most of the SQ-SOA specific stuff (IT-Config, Hystrix, Failover, etc...)
				// we don't need it in the testing environment, and Hystrix is a PITA to get to run correctly
				.connectorProvider(new JettyConnectorProvider());
	}

	private ServiceConfig buildServiceConfig(String artifactId, String serviceName, String username, String password) {
		String ip = runtime.getContainer(artifactId).getIp();
		URL url = null;
		try {
			url = new URL("http://" + ip + "/" + artifactId);
		}
		catch (MalformedURLException e) {
			// Should not happen, unless the developer made some kind of silly mistake
			throw new RuntimeException(e);
		}

		return ServiceConfig.serviceConfig()
				.baseUris(Collections.singleton(URI.create(url.toExternalForm() + "/api/" + serviceName)))
				.connectTimeoutMillis(2000)
				.readTimeoutMillis(20000)
				.totalTimeoutMillis(22000)
				.defaultAsyncExecutorPoolSize(2)
				.username(username)
				.password(password)
				.build();
	}

	public <T> T getSqSoaResource(String artifactId, String serviceName, String username, String password, Class<T> resourceInterface,
			boolean fixServiceName) {
		ClientConfig clientConfig = buildClientConfig();
		SqSoaWebTargetFactory factory = new SqSoaWebTargetFactory(clientConfig);
		WebProxyBuilder<T> proxyBuilder = new WebProxyBuilder<>(resourceInterface)
				.serviceName(serviceName)
				.fixServiceName(fixServiceName)
				.config(buildServiceConfig(artifactId, serviceName, username, password))
				.webTargetFactory(factory);
		return proxyBuilder.build();
	}

	Container getContainer(final String artifactId) {
		return runtime.getContainer(artifactId);
	}

	void pull() {
		log.info("Updating all images to their latest version...");
		runtime.pull(PullPolicy.IF_REGISTRY_IMAGE_NEWER);
	}

	void start() {
		runtime.start();
	}

	void start(String serviceName) {
		runtime.start(serviceName);
	}

	void stop() {
		runtime.stop();
		runtime.clean();
		logEvents.clear();
	}

	void waitUntilReady() {
		waitUntilRmqReady();
		waitUntilSoaReady();
	}

	private void waitUntilRmqReady() {
		logEvents.waitForEntry("sq-rabbitmq", "Server startup complete", TWO_MINUTES, 100);
	}

	void waitUntilSoaReady() {
		logEvents.waitForEntry(
				"sq-serialization-json-e2e-tests-soa-soa",
				"org.apache.catalina.startup.Catalina.start Server startup in", TWO_MINUTES,
				100);
	}

	@Provider
	private static class SimpleClientLoggingFilter implements ClientRequestFilter {

		@Override
		public void filter(ClientRequestContext context) {
			System.out.println("> " + context.getMethod() + " " + context.getUri().toASCIIString());
		}
	}

}
