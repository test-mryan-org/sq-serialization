package com.swissquote.foundation.serialization.test.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.swissquote.foundation.serialization.api.v1.entities.ComplexData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RMQServiceActivator {

	private static final int QUEUE_SIZE = 5;
	private static final int QUEUE_WAIT_TIME = 5;

	private static BlockingQueue<ComplexData> complexDataQueue = new ArrayBlockingQueue(QUEUE_SIZE);
	private static BlockingQueue<Map> mapQueue = new ArrayBlockingQueue(QUEUE_SIZE);
	private static BlockingQueue<Collection> collectionQueue = new ArrayBlockingQueue(QUEUE_SIZE);

	public static List<ComplexData> waitForComplexData() throws InterruptedException {
		List<ComplexData> queueContent = new ArrayList<>();
		ComplexData complexData;
		while ((complexData = complexDataQueue.poll(QUEUE_WAIT_TIME, TimeUnit.SECONDS)) != null) {
			queueContent.add(complexData);
		}
		return queueContent;
	}

	public static List<Map> waitForMap() throws InterruptedException {
		List<Map> queueContent = new ArrayList<>();
		Map map;
		while ((map = mapQueue.poll(QUEUE_WAIT_TIME, TimeUnit.SECONDS)) != null) {
			queueContent.add(map);
		}
		return queueContent;
	}

	public static List<Collection> waitForCollection() throws InterruptedException {
		List<Collection> queueContent = new ArrayList<>();
		Collection collection;
		while ((collection = collectionQueue.poll(QUEUE_WAIT_TIME, TimeUnit.SECONDS)) != null) {
			queueContent.add(collection);
		}
		return queueContent;
	}

	public void handleTransformer(Object object) {
		if (object instanceof ComplexData) {
			complexDataQueue.add((ComplexData) object);
		} else if (object instanceof Map) {
			mapQueue.add((Map) object);
		} else if (object instanceof Collection) {
			collectionQueue.add((Collection) object);
		} else {
			log.error("Unsupported object type {}", object.getClass().getName());
		}
	}
}
