package com.swissquote.treasury.itests.environment;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.ConnectionFactory;

/**
 * the context to provide connection to oracle and instantiate db steps.
 */
@Configuration
public class SpringContextConfiguration {

	@Bean
	public ConnectionFactory clientConnectionFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		try {
			factory.useSslProtocol();
			return factory;
		}
		catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory(ConnectionFactory clientConnectionFactory) {
		CachingConnectionFactory factory = new CachingConnectionFactory(clientConnectionFactory);
		factory.setAddresses("sq-rabbitmq.docker:5671");
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setVirtualHost("foundation");
		return factory;
	}

	@Bean
	public SimpleMessageListenerContainer rmqListenerContainer(CachingConnectionFactory cachingConnectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(cachingConnectionFactory);
		container.setQueueNames("transaction.notifications.sqty-transaction-notification-server.q");
		container.setAutoStartup(true);
		container.setMessageListener(TestRuntime.INSTANCE.getTestRuntime().getTestEnvironment().getNotificationAmqpListener());
		return container;
	}

}