package com.swissquote.foundation.serialization.json;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.swissquote.foundation.serialization.json.spi.JsonSerializationProvider;

public class JsonSerialization {

	private static final WeakHashMap<ClassLoader, LinkedHashMap<String, JsonSerializationProvider>> PROVIDERS = new WeakHashMap<>();

	public static JsonSerializationProvider getSerializationProvider(String fullyQualifiedClassName, ClassLoader classLoader) {
		Map<String, JsonSerializationProvider> providers = getProviders(classLoader);

		return providers.computeIfAbsent(fullyQualifiedClassName, key -> loadProvider(key, classLoader));
	}

	public static JsonSerializationProvider getSerializationProvider(String fullyQualifiedClassName) {
		return getSerializationProvider(fullyQualifiedClassName, Thread.currentThread().getContextClassLoader());
	}

	public static JsonSerializationProvider getSerializationProvider() {
		return getSerializationProvider(Thread.currentThread().getContextClassLoader());
	}

	public static JsonSerializationProvider getSerializationProvider(ClassLoader classLoader) {

		Collection<JsonSerializationProvider> providers = getProviders(classLoader).values();

		if (providers.isEmpty()) {
			throw new JsonSerializationException("No implementation of " + JsonSerializationProvider.class.getName() + "found via SPI");
		}

		if (providers.size() > 1) {
			throw new JsonSerializationException("More than one implementation of " + JsonObjectMapper.class.getName() + "has been provided");
		}

		return providers.iterator().next();
	}

	private static Map<String, JsonSerializationProvider> getProviders(ClassLoader classLoader) {
		synchronized (PROVIDERS) {
			return PROVIDERS.computeIfAbsent(classLoader, key ->
					StreamSupport.stream(ServiceLoader.load(JsonSerializationProvider.class, classLoader).spliterator(), false)
							.collect(Collectors.toMap(
									provider -> provider.getClass().getName(),
									Function.identity(),
									(oldValue, newValue) -> oldValue,
									LinkedHashMap::new)));
		}
	}

	protected static JsonSerializationProvider loadProvider(String fullyQualifiedClassName, ClassLoader classLoader) {
		try {
			Class<?> clazz = classLoader.loadClass(fullyQualifiedClassName);
			if (JsonSerializationProvider.class.isAssignableFrom(clazz)) {
				return ((Class<JsonSerializationProvider>) clazz).newInstance();
			} else {
				throw new JsonSerializationException("The specified class [" + fullyQualifiedClassName + "] is not a JsonSerializationProvider");
			}
		}
		catch (Exception e) {
			throw new JsonSerializationException("Failed to load the JsonSerializationProvider [" + fullyQualifiedClassName + "]", e);
		}

	}

}
