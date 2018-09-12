package com.swissquote.foundation.serialization.json.support.spring.integration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import lombok.Getter;

@Getter
@MessageEndpoint
public class OutboundEndpoint {

	private List<List<Event>> events = new LinkedList<>();

	@ServiceActivator(inputChannel = "outbound-channel")
	public void handle(ArrayList<Event> event) {
		events.add(event);
	}

}
