package com.swissquote.foundation.serialization.test.environment;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionNotificationAmqpListener implements MessageListener {

	@Getter
	private final List<Message> notifications = new CopyOnWriteArrayList<>();

	@Override
	public void onMessage(Message message) {
		try {
			log.info("AMQP essage received {}", message);
			notifications.add(message);
		}
		catch (Exception e) {
			log.warn("Unsupported RMQ message received: {}, {}", message, e);
		}
	}

	public TransactionNotificationAmqpListener clear() {
		notifications.clear();
		return this;
	}

}
